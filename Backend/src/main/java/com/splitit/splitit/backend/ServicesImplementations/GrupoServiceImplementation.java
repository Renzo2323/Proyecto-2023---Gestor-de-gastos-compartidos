package com.splitit.splitit.backend.ServicesImplementations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.AuditoriaGrupo;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Categoria;
import com.splitit.splitit.backend.DAO.CategoriaGrupo;
import com.splitit.splitit.backend.DAO.Deuda;
import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.MetodoDivision;
import com.splitit.splitit.backend.DAO.Pago;
import com.splitit.splitit.backend.DAO.TipoGrupo;
import com.splitit.splitit.backend.DAO.TipoMoneda;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.DtActualizarGrupo;
import com.splitit.splitit.backend.DTO.DtAvatar;
import com.splitit.splitit.backend.DTO.DtCategoria;
import com.splitit.splitit.backend.DTO.DtCategoriaGrupo;
import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.DTO.DtGrupoDeGastos;
import com.splitit.splitit.backend.DTO.DtPago;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.Pagador;
import com.splitit.splitit.backend.DTO.Participante;
import com.splitit.splitit.backend.Exceptions.AvatarTieneUsuarioException;
import com.splitit.splitit.backend.Exceptions.MailingException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarEnGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarException;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;
import com.splitit.splitit.backend.Repository.AuditoriaGrupoRepository;
import com.splitit.splitit.backend.Repository.AvatarRepository;
import com.splitit.splitit.backend.Repository.CategoriaRespository;
import com.splitit.splitit.backend.Repository.DeudaRepository;
import com.splitit.splitit.backend.Repository.GastoCompartidoRepository;
import com.splitit.splitit.backend.Repository.GrupoRepository;
import com.splitit.splitit.backend.Repository.PagoRepository;
import com.splitit.splitit.backend.Repository.ParametroRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.EmailService;
import com.splitit.splitit.backend.Services.GrupoService;

import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;

@Service
public class GrupoServiceImplementation implements GrupoService{
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AvatarRepository avatarRepository;
	
	@Autowired
	private CategoriaRespository categoriaRepository;
	
	@Autowired
	private PagoRepository pagoRespoitory;
	
	@Autowired
	private GastoCompartidoRepository gastoCompartidoRepository;
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	@Autowired
	private DeudaRepository deudaRepository;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private AuditoriaService auditoriaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AuditoriaGrupoRepository auditoriaRepository;
	
	@Autowired
	public GrupoServiceImplementation(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}
	
	@Override
	public DtResponse addGrupo(int usuarioId, DtGrupoDeGastos dtGrupo) {
		DtResponse respuesta =  new DtResponse("", false);
    	respuesta = validarDatosGrupo(dtGrupo);
    	if(!respuesta.getEstatus()) return respuesta;
		
		
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
				
		GrupoDeGastos grupo = new GrupoDeGastos(
				dtGrupo.getId(), 
				dtGrupo.getNombre(), 
				dtGrupo.getTipoGrupo(), 
				dtGrupo.getImagen(), 
				dtGrupo.getMoneda()
				);
		
		grupoRepository.save(grupo);
		Avatar avatar = new Avatar();
		if (usuario.isPresent()){
			Usuario usuarioObj = usuario.get();
			if (usuarioObj instanceof UsuarioFinal) {
				//Creo avatar
				avatar = new Avatar(usuarioObj.getNombre(), 0F, grupo, (UsuarioFinal)usuarioObj); 
				avatarRepository.save(avatar);
				
				//Agrego el avatar a la lista del usuario
				((UsuarioFinal)usuarioObj).addAvatar(avatar);

				//SE AGREGA AUDITORIA
				String detalle = null;
				auditoriaService.addAuditoriaGrupo(grupo.getId(), AuditoriaAccion.NUEVOGRUPO, avatar, 0, detalle);
				
			}	
		}
		String mensaje = "Grupo creado con exito, id:" + grupo.getId();

		respuesta = new DtResponse(mensaje,true);
        return respuesta;	
	}
	
	@Override
	public DtResponse addMiGrupo(DtGrupoDeGastos dtGrupo) {
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		return addGrupo(usuario.getId(), dtGrupo);
	}
	

	@Override
	@Transactional
	public DtResponse deleteGrupo(int grupoId){

		try {

			Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
			if(grupo.isPresent()) {

				GrupoDeGastos grupoObj = grupo.get();
				
				//Obtengo y elimino auditorias del grupo
				eliminarAuditoriasGrupo(grupoObj);

				//Obtengo y elimino los gastos compartidos 
				eliminarGastosCompartidosDeGrupo(grupoObj);
				
				//Obtengo y elimino las categorias del grupo
				eliminarCategoriasDeGrupo(grupoObj);

				//obtengo y elimino pagos del grupo
				eliminarPagosDeGrupo(grupoObj);


				//Eliminar avatares miembros de un grupo
				eliminarAvataresDeGrupo(grupoObj);
				
				grupoRepository.save(grupoObj);
				
				
				grupoRepository.delete(grupoObj);
				
			}
			DtResponse respuesta = new DtResponse("Grupo eliminado con exito",true);

	        return respuesta;	
		}catch(Exception e) {
			DtResponse respuesta = new DtResponse(e.getMessage(),false);
	        return respuesta;
			
		}
	}
	
	@Transactional
	private void eliminarCategoriasDeGrupo(GrupoDeGastos grupoObj) {
		List<CategoriaGrupo> categoriasGrupo = grupoObj.getCategoriasGrupo();
		for (CategoriaGrupo categoriaGrupo : categoriasGrupo) {
			categoriaRepository.delete(categoriaGrupo);
		}
	}
	
	@Transactional
	private void eliminarPagosDeGrupo(GrupoDeGastos grupoObj) {
		List<Pago> listaPagos = grupoObj.getPagosGrupo();
		for (Pago pagosGrupo : listaPagos) {
			pagoRespoitory.delete(pagosGrupo);

		}
		
	}
	
	@Transactional
	private void eliminarGastosCompartidosDeGrupo(GrupoDeGastos grupoObj) {
		List<GastoCompartido> listaGastos = grupoObj.getGastosCompartidos();
		for (GastoCompartido gastoCompartido : listaGastos) {
			eliminarDeudasDeGastosCompartidos(gastoCompartido);
			gastoCompartidoRepository.delete(gastoCompartido);
		}
		
	}
	
	@Transactional
	private void eliminarDeudasDeGastosCompartidos(GastoCompartido gasto) {
		List<Deuda> deudas = gasto.getDeudas();
		for(Deuda deuda: deudas) {
			deudaRepository.delete(deuda);
		}
		gastoCompartidoRepository.save(gasto);
	}
	
	@Transactional
	private void eliminarAvataresDeGrupo(GrupoDeGastos grupoObj) {
		List<Avatar> listaAvatar = avatarRepository.findAll();
		for (Avatar avatar : listaAvatar) {
			if (grupoObj.getId() == avatar.getIdGrupoDeGastos()) {
				avatarRepository.delete(avatar);
			}			
		}
	}
	
	@Transactional
	private void eliminarAuditoriasGrupo(GrupoDeGastos grupoObj){
		List<AuditoriaGrupo> auditorias = grupoObj.getAuditoria();
		for(AuditoriaGrupo auditora : auditorias) {
			auditoriaRepository.delete(auditora);
		}

	}
	

	@Override
	public List<DtGrupoDeGastos> getGrupos() {
		List<GrupoDeGastos> listaGrupos = grupoRepository.findAll();
		List<DtGrupoDeGastos> listaDtGrupos = new ArrayList<DtGrupoDeGastos>();
		for (GrupoDeGastos grupo : listaGrupos) {
			DtGrupoDeGastos dtGrupo = new DtGrupoDeGastos(grupo.getId(), grupo.getNombre(), grupo.getTipoGrupo(), grupo.getImagen(), grupo.getMoneda());
			listaDtGrupos.add(dtGrupo);
		}
		return listaDtGrupos;
	}

	@Override
	public List<DtGrupoDeGastos> getGrupoByUsuario(int usuarioId) {
		List<DtGrupoDeGastos> listaDtGrupos = new ArrayList<DtGrupoDeGastos>();	
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		if (usuario.isPresent()){
			Usuario usuarioObj = usuario.get();
			if (usuarioObj instanceof UsuarioFinal) {
				List<Avatar> listaAvatares = ((UsuarioFinal) usuarioObj).getAvatares();
				for(Avatar avatar : listaAvatares) {
					GrupoDeGastos grupo = avatar.getGrupoGastos();
					DtGrupoDeGastos dtGrupo = new DtGrupoDeGastos(grupo.getId(), grupo.getNombre(), grupo.getTipoGrupo(), grupo.getImagen(), grupo.getMoneda(), avatar.getSaldoLocal());
					listaDtGrupos.add(dtGrupo);					
				}
				
			}
		}
		return listaDtGrupos;
	}
	
	@Override
	public List<DtGrupoDeGastos> getMisGrupos() {
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		return getGrupoByUsuario(usuario.getId());
	}

	@Override
	public boolean grupoTieneSaldos(int grupoId) {
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
		if(grupo.isPresent()) {
			//ToDo Como calcular saldo de un grupo 
		}

		
		return false;
	}

	@Override
	public ResponseEntity<?> crearAvatarEnGrupo(int grupoId, String nombre) {
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
		if(grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			Avatar avatarCreador = getAvatardeUsuarioLogueado(grupoObj);
			
			Avatar avatar = new Avatar(nombre, 0F, grupoObj, null);
			int avatarCreadoId = avatarRepository.save(avatar).getId();
			
			grupoObj.addAvatarAGrupo(avatar);
			
			//SE AGREGA AUDITORIA
			String detalle = nombre;
			auditoriaService.addAuditoriaGrupo(grupoObj.getId(), AuditoriaAccion.NUEVOAVATAR, avatarCreador, avatar.getId(), detalle);
			
			
			return ResponseEntity.ok(avatarCreadoId);
			
		}else {
			return ResponseEntity.ok(new DtResponse("No se pudo crear la persona",false));
		}	
	}

//	@Override
//	public DtResponse aceptarInvitacion(int usuarioId, int grupoId) {
//		DtResponse respuesta = new DtResponse("",false);
//		
//		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
//		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
//
//		if (usuario.isPresent()){
//			Usuario usuarioObj = usuario.get();
//			
//			if(grupo.isPresent()) {
//				GrupoDeGastos grupoObj = grupo.get();
//				
//				Avatar avatar = new Avatar(usuarioObj.getNombre() + ' ' + usuarioObj.getApellido(), 0F, grupoObj, (UsuarioFinal)usuarioObj);
//				avatarRepository.save(avatar);
//				
//				grupoObj.addAvatarAGrupo(avatar);
//				respuesta = new DtResponse("Invitacion aceptada correctamente",true);
//			}else {
//				respuesta = new DtResponse("No se encontro el grupo",false);
//			}			
//		}else {
//			respuesta = new DtResponse("No se encontro el usuario",false);
//		}		
//		return respuesta;
//	}
	
	@Override
	public DtResponse invitarUsuarioAGrupo(int grupoId, int avatarId, String email) throws NoExisteAvatarEnGrupoException, NoExisteGrupoException, NoExisteAvatarException, AvatarTieneUsuarioException {
		Optional<GrupoDeGastos> grupoOpt = grupoRepository.findById(grupoId);
		if (grupoOpt.isPresent()) {
			GrupoDeGastos grupo = grupoOpt.get();
			
			Optional<Avatar> avatarInvitadoOpt = avatarRepository.findById(avatarId);
			if (avatarInvitadoOpt.isPresent()) {
				Avatar avatarInvitado = avatarInvitadoOpt.get();
				
				List<Avatar> avataresDeGrupo = grupo.getAvataresDeGrupo();
				if (avataresDeGrupo.contains(avatarInvitado)) {
					
					if (avatarInvitado.getUsuario()==null) {
						String shortUUID = RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz") + "-" + RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz");
						avatarInvitado.setTokenInvitacion(shortUUID.toString());
						
						avatarRepository.save(avatarInvitado);
						
						String invitacionURL = generateInvitacionURL(avatarInvitado.getTokenInvitacion());
						try {
							emailService.sendInvitacionToken(email, invitacionURL);
						} catch (MailingException e) {
							e.printStackTrace();
							return new DtResponse("Invitacion creada con exito. Error al enviar el mensaje. El codigo de invitacion es: " + shortUUID, false);
						}
						
						Avatar avatarCreador = getAvatardeUsuarioLogueado(grupo);
						
						//SE AGREGA AUDITORIA
						String detalle = email;
						auditoriaService.addAuditoriaGrupo(grupo.getId(), AuditoriaAccion.NUEVAINVITACION, avatarCreador, 0, detalle);
						
						
						return new DtResponse("Invitacion creada con exito. El codigo de invitacion es: " + shortUUID, true);
					}
					else {
						throw new AvatarTieneUsuarioException("Este usuario ya es un participante activo del grupo");
					}
				}
				else {
					throw new NoExisteAvatarEnGrupoException();
				}
			}
			else {
				throw new NoExisteAvatarException("El avatar con id " + avatarId + " no existe en el sistema");
			}
		}
		else {
			 throw new NoExisteGrupoException("El grupo con id " + grupoId + " no existe en el sistema");
		}	 
	}
	
	private String generateInvitacionURL(String token) {
		String protocoloURL = parametroRepository.getByNombre("PROTOCOLO_FRONTEND").getValor();
		String hostURL = parametroRepository.getByNombre("HOST_FRONTEND").getValor();
		String puertoURL = parametroRepository.getByNombre("PUERTO_FRONTEND").getValor();


		return protocoloURL + "://" + hostURL + ":" + puertoURL + "/AceptarInvitacion/" + token;
	}

	@Override
	public List<TipoGrupo> getTipoGrupos() {
		List<TipoGrupo> result = Arrays.asList(TipoGrupo.class.getEnumConstants());
		return result;
	}

	
	private DtResponse validarDatosGrupo(DtGrupoDeGastos dtGrupo) {
		DtResponse respuesta =  new DtResponse("", false);
		if(dtGrupo.getNombre().isEmpty()) {
			respuesta = new DtResponse("El nombre no puede ser vacio", false);
			return respuesta;
		}
		
		
		respuesta = new DtResponse("Datos correctos", true);
		return respuesta;
	}

	@Override
	public List<TipoMoneda> getTipoMoneda() {
		List<TipoMoneda> result = Arrays.asList(TipoMoneda.class.getEnumConstants());
		return result;
	}
	@Override
	public DtGrupoDeGastos obtenerInfoGrupo(int grupoId) throws NoExisteAvatarEnGrupoException{ 
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
		DtGrupoDeGastos dtGrupo = new DtGrupoDeGastos();
		if(grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			
			if(usuarioPerteneceGrupo(grupoObj) == false) {
				throw new NoExisteAvatarEnGrupoException();
			}

			dtGrupo.setId(grupoObj.getId());
			dtGrupo.setNombre(grupoObj.getNombre());
			dtGrupo.setTipoGrupo(grupoObj.getTipoGrupo());
			dtGrupo.setImagen(grupoObj.getImagen());
			dtGrupo.setMoneda(grupoObj.getMoneda());
			
			//Cargo categorias del grupo
			List<CategoriaGrupo> categoriasGrupo = grupoObj.getCategoriasGrupo();
			for(CategoriaGrupo categoria : categoriasGrupo) {
				DtCategoriaGrupo dtCategoria = new DtCategoriaGrupo(categoria.getId(), categoria.getNombre(), categoria.getIcono(), categoria.getDescripcion(), categoria.getEstado());
				dtGrupo.addCategoriaGrupo(dtCategoria);
			}
			
			//Cargo GastosCompartidos del grupo
			List<GastoCompartido> gastosComaprtidos =  grupoObj.getGastosCompartidos();
			for(GastoCompartido gasto : gastosComaprtidos) {
				
				Categoria categoriaGasto = gasto.getCategoria();
				DtCategoria dtCategoriaGasto = new DtCategoria(categoriaGasto.getId(), categoriaGasto.getNombre(), categoriaGasto.getIcono(), categoriaGasto.getDescripcion(), categoriaGasto.getEstado());
				
				GrupoDeGastos grupoGasto = gasto.getGrupoGastos();
				
				DtGastoCompartido dtGasto = new DtGastoCompartido(gasto.getId(), grupoGasto.getId(), gasto.getDescripcion(), gasto.getMonto(), gasto.getMoneda(), gasto.getNota(), gasto.getImagen(), dtCategoriaGasto, gasto.getMetodoDivision());
				
				List<Participante> participantes = new ArrayList<Participante>();
				List<Pagador> pagadores = new ArrayList<Pagador>();
				List<Deuda> deudas = gasto.getDeudas();
				
				MetodoDivision metodoDivision = gasto.getMetodoDivision();
				float sumaDeudas = 0;
				for(Deuda deuda : deudas) {
					int idPagador = deuda.getAcreedor().getId();
					int idDeudor = deuda.getDeudor().getId();
					sumaDeudas += deuda.getMonto();
					
					boolean yaCargadoPagador = false;
					boolean yaCargadoParticipante = false;
					
					//PAGADORES
					for(Pagador pagador : pagadores) {
						if(deuda.getAcreedor().getId() == pagador.getAvatar()) {
							pagador.setPago(pagador.getPago() + deuda.getMonto());
							yaCargadoPagador = true;
							break;
						}
					}
					if(!yaCargadoPagador) {
						Pagador pagador = new Pagador(deuda.getAcreedor().getId(), deuda.getMonto());
						pagadores.add(pagador);
					}
					
					//PARTICIPANTES
					for(Participante participante : participantes) {
						if(deuda.getDeudor().getId() == participante.getAvatar()) {
							participante.setCuotaParticipacion(participante.getCuotaParticipacion() + deuda.getMonto());  
							yaCargadoParticipante = true;
							break;
						}
					}
					if(!yaCargadoParticipante) {
						Participante participante = new Participante(deuda.getDeudor().getId(), deuda.getMonto());
						participantes.add(participante);
					}	
				}
			/*	
				if(sumaDeudas != gasto.getMonto()) {
					if(pagadores.size() == 1) {
						switch(metodoDivision) {
							case PARTES_IGUALES:
								int cantParticipantes = participantes.size() + 1;
								float montoPorParticipante = gasto.getMonto() / cantParticipantes;
								for(Pagador pagador : pagadores) {
									pagador.setPago(gasto.getMonto());
									Participante participante = new Participante(pagador.getAvatar(), montoPorParticipante);
									participantes.add(participante);
								}
								break;
							case CANTIDAD_EXACTA:
								
								
								
							case PORCENTAJE:
								float montoPagador = gasto.getMonto() - sumaDeudas;
								for(Pagador pagador : pagadores) {
									pagador.setPago(gasto.getMonto());
									Participante participante = new Participante(pagador.getAvatar(), montoPagador);
									participantes.add(participante);
								}
								break;
				
						}
					}
				}
				*/
				dtGasto.setPagador(pagadores);
				dtGasto.setParticipantes(participantes);
				
				
				dtGrupo.addGastoCompartido(dtGasto);			
			}
			
			 
			//Cargo Pagos del grupo
			List<Pago> pagos = grupoObj.getPagosGrupo();
			for(Pago pago: pagos) {

				int avatarPagador = pago.getPagador().getId();
				int avatarReceptor = pago.getReceptor().getId();

				DtPago dtPago = new DtPago(pago.getId(), pago.getMonto(), pago.getMoneda(), pago.getMetodoPago(), pago.getNota(), pago.getFecha(), grupoObj.getId(),
						avatarPagador, avatarReceptor);
				
				dtGrupo.addPago(dtPago);				
			}
			
			//Cargo los participantes
			List<Avatar> avatares = grupoObj.getAvataresDeGrupo();
			boolean esUsuarioActual;
			for(Avatar avatar : avatares) {
				esUsuarioActual = false;
				Usuario usuarioAvatar = avatar.getUsuario();
				DtAvatar dtAvatar = new DtAvatar();
				if(usuarioAvatar != null) {
					if(usuario != null && usuarioAvatar.getId() == usuario.getId()) {
						esUsuarioActual = true;
					}		
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), esUsuarioActual, true, usuarioAvatar.getImagen());
				}else {
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), esUsuarioActual, false, null);
				}
				dtGrupo.addParticipante(dtAvatar);
			}
			
		}
		return dtGrupo;
	}
	
	@Override
	public DtGrupoDeGastos obtenerInfoGrupoByInvitacionToken(String token) throws NoExisteGrupoException {
		GrupoDeGastos grupo = obtenerGrupoByInvitacionToken(token);
		if (grupo != null) {
			DtGrupoDeGastos grupoDT = new DtGrupoDeGastos();
			grupoDT.setId(grupo.getId());
			grupoDT.setNombre(grupo.getNombre());
			grupoDT.setImagen(grupo.getImagen());
			//Obtengo lista de avatares
			List<Avatar> avataresDeGrupo = grupo.getAvataresDeGrupo();
			//Creo lista de DTAvatar y la inicializo vacia
			List<DtAvatar> avataresDT = new ArrayList<DtAvatar>();
			//recorro lista de avatares, creo DTAvatar y lo agrego a la lista de DTAvatar
			for (Avatar avatarAux : avataresDeGrupo) {
				DtAvatar avatarDT = new DtAvatar();
				avatarDT.setNombre(avatarAux.getNombre());
				//no guardar los avatares no confirmados (sin usuario)
				if (avatarAux.getUsuario() != null) {
					avatarDT.setImagen(avatarAux.getUsuario().getImagen());
				
					avataresDT.add(avatarDT);
				}
			}
			//seteo lista de participantes
			grupoDT.setParticipantes(avataresDT);
			
			return grupoDT;
		}
		else {
			throw new NoExisteGrupoException("Error al obtener grupo por token");
		}
	}
	
	private GrupoDeGastos obtenerGrupoByInvitacionToken(String token) {
		Optional<Avatar> avatarOpt = avatarRepository.findByTokenInvitacion(token);
		
		if (avatarOpt.isPresent()) {
			Avatar avatar = avatarOpt.get();
			return avatar.getGrupoGastos();
		}
		else {
			
			return null;
		}
	}
	
	private boolean validarUsuarioPerteneceAGrupo(UsuarioFinal usuario, int grupoId) {
		List<Avatar> avataresDeUsuario = usuario.getAvatares();
		for(Avatar avatar : avataresDeUsuario) {
			if(avatar.getGrupoGastos().getId() == grupoId) {
				return true;
			}
		}
		
		return false;
	}
	
	private float getSaldoLocalAvatar(Avatar avatar) {
		return 0;
	}

	@Override
	public DtResponse actualizarGrupo(int idGrupo, DtActualizarGrupo dtGrupo) throws NoExisteGrupoException {
		GrupoDeGastos aux_grupo = grupoRepository.findById(idGrupo).orElseThrow(NoExisteGrupoException::new);
		Usuario aux_usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		if(usuarioPerteneceGrupo(aux_grupo) == false) {
			return new DtResponse("El usuario no pertenece al grupo", false);
		}
		aux_grupo.setNombre(dtGrupo.getNombre());
		aux_grupo.setImagen(dtGrupo.getImagen());
		aux_grupo.setTipoGrupo(dtGrupo.getTipoGrupo());
//		aux_grupo.setMoneda(dtGrupo.getMoneda());    La moneda no se debe poder cambiar
		grupoRepository.save(aux_grupo);
		return new DtResponse("Datos actualizados correctamente", true);
	}
	
	public boolean usuarioPerteneceGrupo(GrupoDeGastos grupo) {
		boolean pertenece = false;
		UsuarioFinal aux_usuario = (UsuarioFinal) usuarioRepository.findByEmail(jwtUtil.getEmail());
		List<Avatar> avataresUsuario = aux_usuario.getAvatares();
		List<Avatar> avataresGrupo = grupo.getAvataresDeGrupo();
		for(Avatar avatar : avataresUsuario) {
			for(Avatar avatarGrupo : avataresGrupo) {
				if(avatarGrupo.equals(avatar)) {
					pertenece = true;
				}
			}
		}
		return pertenece;
	}

	public Avatar getAvatardeUsuarioLogueado(GrupoDeGastos grupo) {
		UsuarioFinal aux_usuario = (UsuarioFinal) usuarioRepository.findByEmail(jwtUtil.getEmail());
		List<Avatar> avataresUsuario = aux_usuario.getAvatares();
		List<Avatar> avataresGrupo = grupo.getAvataresDeGrupo();
		for(Avatar avatar : avataresUsuario) {
			for(Avatar avatarGrupo : avataresGrupo) {
				if(avatarGrupo.equals(avatar)) {
					return avatar;
				}
			}
		}
		return null;
	}
	
}
