package com.splitit.splitit.backend.ServicesImplementations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Categoria;
import com.splitit.splitit.backend.DAO.Deuda;
import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.MetodoDivision;
import com.splitit.splitit.backend.DAO.NotificacionUsuario;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.CantGastosPorCategoria;
import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.Pagador;
import com.splitit.splitit.backend.DTO.Participante;
import com.splitit.splitit.backend.Notifications.OneSignal;
import com.splitit.splitit.backend.Notifications.TipoInternoNotificacion;
import com.splitit.splitit.backend.Repository.AvatarRepository;
import com.splitit.splitit.backend.Repository.CategoriaRespository;
import com.splitit.splitit.backend.Repository.DeudaRepository;
import com.splitit.splitit.backend.Repository.GastoCompartidoRepository;
import com.splitit.splitit.backend.Repository.GrupoRepository;
import com.splitit.splitit.backend.Repository.NotificacionUsuarioRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.EmailService;
import com.splitit.splitit.backend.Services.GastoCompartidoService;
import com.splitit.splitit.backend.WebSocket.NotificacionController;

@Service
public class GastoCompartidoServiceImplementation implements GastoCompartidoService {
	
	@Autowired
	private GastoCompartidoRepository gastoCompartidoRepo;
	
	@Autowired
	private CategoriaRespository categoriaRepo;
	
	@Autowired
	private GrupoRepository grupoGastosRepo;
	
	@Autowired
	private DeudaRepository deudaRepository;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Autowired
    private JWTUtil jwtUtil;
    
    private AuditoriaService auditoriaService;
    
    @Autowired
    private AvatarRepository avatarRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	public GastoCompartidoServiceImplementation(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}
	
	@Autowired
	private NotificacionController notificacionController;
	
	@Autowired
	private NotificacionUsuarioRepository notiUsuRepo;

	
	@Override
	public DtResponse nuevoGastoCompartido(DtGastoCompartido datosGastoCompartido) {	
		try {
			Categoria categoria = categoriaRepo.findById(
					datosGastoCompartido.getCategoria().getId()).get();
			
			GrupoDeGastos grupoGastos = grupoGastosRepo.findById(
					datosGastoCompartido.getGrupoid()).get();
			
			//Creo el gasto con lo que viene
			GastoCompartido nuevoGasto = new GastoCompartido(
					datosGastoCompartido.getDescripcion(),
					datosGastoCompartido.getMonto(),
					grupoGastos.getMoneda(),
					datosGastoCompartido.getImagen(),
					datosGastoCompartido.getMetodoDivision(),
					datosGastoCompartido.getNota(),
					categoria,
					datosGastoCompartido.getPagadores().size(),
					datosGastoCompartido.getParticipantes().size(),
					grupoGastos
					);
			
			gastoCompartidoRepo.save(nuevoGasto); // -------> Se deberia hacer un rollback si todo lo de abajo falla
			
			MetodoDivision metodoDivision = datosGastoCompartido.getMetodoDivision();
			List<Pagador> pagadores = datosGastoCompartido.getPagadores();
			List<Participante> participantes = datosGastoCompartido.getParticipantes();
			
			int cantidadParticipantes = participantes.size();
			
			float montoPorParticipante = 0;
			float monto = 0;
			for(Pagador pagador: pagadores) {
				@SuppressWarnings("deprecation")
				Avatar acreedor = avatarRepository.getById(pagador.getAvatar());
				if(!avatarPerteneceAGrupo(grupoGastos, acreedor.getId())) {
					return new DtResponse("El avatar " + acreedor.getId() + " no pertenece al grupo." , true);
				}
				if(metodoDivision.equals(MetodoDivision.PARTES_IGUALES)) {
					montoPorParticipante = ((pagador.getPago()) / cantidadParticipantes);
				}
				
				acreedor.setSaldoLocal(acreedor.getSaldoLocal() + pagador.getPago());
				
				for(Participante participante : participantes) {
					Deuda deuda = new Deuda();
					@SuppressWarnings("deprecation")
					Avatar deudor = avatarRepository.getById(participante.getAvatar());
					if(!avatarPerteneceAGrupo(grupoGastos, deudor.getId())) {
						return new DtResponse("El avatar " + deudor.getId() + " no pertenece al grupo." , true);
					}
					switch(metodoDivision) {
						case PARTES_IGUALES:
							deuda = new Deuda(montoPorParticipante, montoPorParticipante, deudor, acreedor, nuevoGasto);
							
							deudor.setSaldoLocal(deudor.getSaldoLocal() - montoPorParticipante);
							break;
						case CANTIDAD_EXACTA:
//							if(pagadores.size() == 1) {
//								deuda = new Deuda(participante.getCuotaParticipacion(), participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
//								
//								deudor.setSaldoLocal(deudor.getSaldoLocal() - montoPorParticipante);
//								
//							}else {
//								
//								float porcentajeParticipacionPagador = ((pagador.getPago() * 100) / nuevoGasto.getMonto());
//								
//								float montoParticipante = ((participante.getCuotaParticipacion() * porcentajeParticipacionPagador) / 100);
//								
//								deuda = new Deuda(montoParticipante, participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
//								
//								deudor.setSaldoLocal(deudor.getSaldoLocal() - montoPorParticipante);
//							
//							}
							
							float loQueMeDeben = pagador.getPago() - participante.getCuotaParticipacion();
							if(loQueMeDeben < 0) {
								//El participante me da la parte que necesito y estoy satisfecho
								//Pero el participante aun tiene para dar, asi que queda para otro pagador
								deuda = new Deuda(pagador.getPago(), participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
								deudor.setSaldoLocal(deudor.getSaldoLocal() - pagador.getPago());
								pagador.setPago(0);
								participante.setCuotaParticipacion(loQueMeDeben *-1);								
								break;
							}else if(loQueMeDeben == 0) {
								//El participante me da exactamente lo que necesito
								//Estoy satisfecho, y tambien lo esta el participante
								deuda = new Deuda(pagador.getPago(), participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
								deudor.setSaldoLocal(deudor.getSaldoLocal() - pagador.getPago());
								pagador.setPago(0);
								participante.setCuotaParticipacion(0);								
								break;
							}else {
								//Aun me queda para que me pagen, no estoy satisfecho
								//Pero el participante si lo esta, queda en 0
								deuda = new Deuda(participante.getCuotaParticipacion(), participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
								deudor.setSaldoLocal(deudor.getSaldoLocal() - participante.getCuotaParticipacion());
								pagador.setPago(loQueMeDeben);
								participante.setCuotaParticipacion(0);																
							}
			
							break;
							
						case PORCENTAJE:
							monto = ((pagador.getPago() * participante.getCuotaParticipacion()) / 100);
							
							deuda = new Deuda(monto, participante.getCuotaParticipacion(), deudor, acreedor, nuevoGasto);
							
							deudor.setSaldoLocal(deudor.getSaldoLocal() - monto);
							break;
					}
					deudaRepository.save(deuda);
				}
			}

			//AUDITORIA
			Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
			if(usuario !=  null) {
				
				Avatar avatar = getAvatarDeUsuarioDeGrupo(grupoGastos, usuario.getId());	
				
				String detalle = "monto " + grupoGastos.getMonedaSimbolo() +  datosGastoCompartido.getMonto();
				auditoriaService.addAuditoriaGrupo(grupoGastos.getId(), AuditoriaAccion.NUEVOGASTO, avatar, nuevoGasto.getId(), detalle);

				

			}
			
			List<Usuario> usuariosNotificados = new ArrayList<Usuario>();
			for(Pagador pagador: pagadores) {
				@SuppressWarnings("deprecation")
				Avatar avatarPagador = avatarRepository.getById(pagador.getAvatar());
				Usuario usuarioPagador = avatarPagador.getUsuario();
				if(usuarioPagador != null && !avatarEsUsuarioActual(avatarPagador)) {
					if(!usuariosNotificados.contains(usuarioPagador)) {
						usuariosNotificados.add(usuarioPagador);
					}
					
				}
				
			}
			
			for(Participante participante: participantes) {
				@SuppressWarnings("deprecation")
				Avatar avatarParticipante = avatarRepository.getById(participante.getAvatar());
				Usuario usuarioParticipante = avatarParticipante.getUsuario();
				if(usuarioParticipante != null && !avatarEsUsuarioActual(avatarParticipante)) {
					if(!usuariosNotificados.contains(usuarioParticipante)) {
						usuariosNotificados.add(usuarioParticipante);
					}
					
				}
				
			}

			String mensajeNotificacion = usuario.getNombre() + " te incluyó en un gasto: " + datosGastoCompartido.getDescripcion();
			for(Usuario usr : usuariosNotificados) {
				NotificacionUsuario notificacionUsuario = new NotificacionUsuario(LocalDateTime.now(), mensajeNotificacion, false, (UsuarioFinal) usr, grupoGastos);
				
				//Se guarda la notificacion en la BD
				notiUsuRepo.save(notificacionUsuario);
				//Se envia por WebSocket
				notificacionController.enviarUsuario(mensajeNotificacion, usr.getId());
				//Se envia por mail
				emailService.sendNotificacionGasto(usr.getEmail(), mensajeNotificacion);
			}
			//ONE SIGNAL - NOTIFICACION A TODOS LOS PARTICIPANTES
			enviarNotificacionPush(TipoInternoNotificacion.NUEVOGASTO, getEmailsParticipantes(usuariosNotificados), mensajeNotificacion);
			
			
			
			return new DtResponse("Gasto compartido creado con exito", true);
		
		}catch(Exception e){
		
			return new DtResponse( e.getMessage(), false);
			
		}	
	
	}
	
	private void enviarNotificacionPush(TipoInternoNotificacion tipo, List<String> emails, String titulo) {
		OneSignal notificacion = new OneSignal();
		notificacion.createNotification(tipo, emails, titulo);
	}
	
	public float calcularMontoCreditoDebitoPorAvatar(float monto, MetodoDivision metodoDivision, int cantAvatares) {
		//TODO calculo del monto del credito (que si no me equivoco deberia ser el mismo que el debito)
		//segun el metodo de division y la cantidad de personas involucradas 
		return 0;
	}
	
	private Avatar getAvatarDeUsuarioDeGrupo(GrupoDeGastos grupo, int usuarioId) {
		List<Avatar> avatares = grupo.getAvataresDeGrupo();
		for(Avatar avatar : avatares) {
			Usuario usuario = avatar.getUsuario();
			if(usuario != null) {
				if(usuario.getId() == usuarioId) {
					return avatar;
				}
			}
			
		}
		return null;
	}
	
	private boolean avatarPerteneceAGrupo(GrupoDeGastos grupo, int avatarId) {
		boolean pertenece = false;
		List<Avatar> avatares = grupo.getAvataresDeGrupo();
		for(Avatar avatar : avatares) {
			if(avatar.getId() == avatarId) {
				pertenece = true;
				break;
			}
		}
		
		return pertenece;
	}


	@Override
	public List<CantGastosPorCategoria> getCantGastosPorCategoria() {
		return gastoCompartidoRepo.getCantGastosCategoria();
	}
	
	private List<String> getEmailsParticipantes(List<Usuario> usuarios){
		List<String> listaEmailsUsuarios = new ArrayList<>();
		for (Usuario usuario : usuarios) {
			listaEmailsUsuarios.add(usuario.getEmail());
		}
		return listaEmailsUsuarios;
	}


	@Override
	public DtResponse eliminarGastoCompartido(int gastoCompartidoId) {
		DtResponse respuesta =  new DtResponse("", false);
		
		try {
			
			GastoCompartido gasto =  gastoCompartidoRepo.findById(gastoCompartidoId).get();
			List<Deuda> deudas = gasto.getDeudas();
			for(Deuda deuda : deudas) {
				Avatar avatarAcreedor = deuda.getAcreedor();
				Avatar avatarDeudor = deuda.getDeudor();
				
				avatarAcreedor.setSaldoLocal(avatarAcreedor.getSaldoLocal() - deuda.getMonto());
				avatarDeudor.setSaldoLocal(avatarDeudor.getSaldoLocal() + deuda.getMonto());

				avatarRepository.save(avatarAcreedor);
				avatarRepository.save(avatarDeudor);
				
				deudaRepository.delete(deuda);
				
			}

			gastoCompartidoRepo.delete(gasto);
			
			respuesta =  new DtResponse("Gasto compartido eliminado correctamente", true);			
		}catch(Exception e){
		
			return new DtResponse( e.getMessage(), false);
			
		}
		
		
		return respuesta;
	}
	
	private boolean avatarEsUsuarioActual(Avatar avatar) {
		Usuario usuarioActual = usuarioRepository.findByEmail(jwtUtil.getEmail());
		Usuario usuarioAvatar = avatar.getUsuario();
		if(usuarioActual != null && usuarioAvatar != null && usuarioAvatar.getId() == usuarioActual.getId()) {
			return true;
		}
		
		return false;
	}

}
