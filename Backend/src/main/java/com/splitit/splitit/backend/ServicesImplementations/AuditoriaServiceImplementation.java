package com.splitit.splitit.backend.ServicesImplementations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.AuditoriaGrupo;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.TipoMoneda;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.AvatarMayorDeudor;
import com.splitit.splitit.backend.DTO.DtAuditoriaGrupo;
import com.splitit.splitit.backend.DTO.DtAuditoriaUsuario;
import com.splitit.splitit.backend.DTO.DtAvatar;
import com.splitit.splitit.backend.DTO.DtEstadisticasUsuario;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.GastoPorCategoriaUsuario;
import com.splitit.splitit.backend.DTO.GastoPorMesUsuario;
import com.splitit.splitit.backend.Exceptions.ExisteUsuarioException;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;
import com.splitit.splitit.backend.DTO.GruposPorDia;
import com.splitit.splitit.backend.DTO.TransaccionesPorDia;
import com.splitit.splitit.backend.Repository.AuditoriaGrupoRepository;
import com.splitit.splitit.backend.Repository.GastoCompartidoRepository;
import com.splitit.splitit.backend.Repository.GrupoRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.AuditoriaService;

@Service
public class AuditoriaServiceImplementation implements AuditoriaService{

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private AuditoriaGrupoRepository auditoriaGrupoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private GastoCompartidoRepository gastoRepo;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Override
	public void addAuditoriaGrupo(int grupoId, AuditoriaAccion accion, Avatar avatar1, int idEntidad, String detalle) {
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
		if(grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			AuditoriaGrupo auditoria = new AuditoriaGrupo(grupoObj, accion, avatar1, idEntidad, detalle);
			auditoriaGrupoRepository.save(auditoria);
		}
	}

	@Override
	public List<DtAuditoriaGrupo> getAuditoriaGrupo(int grupoId) {
		List<DtAuditoriaGrupo> listaDtAuditoria = new ArrayList<DtAuditoriaGrupo>();
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(grupoId);
		if(grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			List<AuditoriaGrupo> listAuditoria = grupoObj.getAuditoria();
			for (AuditoriaGrupo auditoria : listAuditoria) {
				
				DtAuditoriaGrupo dtAuditoria = new DtAuditoriaGrupo();
				dtAuditoria.setFecha(auditoria.getFecha());
				dtAuditoria.setAccion(auditoria.getAccion());
				
				Avatar avatar = auditoria.getAvatar();
				String nombreAvatar = "";
				if(avatar != null) {
					nombreAvatar = avatar.getNombre();					
				}
				String detalle = auditoria.getDetalle();
				int idEntidad = auditoria.getIdEntidad();
				//Timestamp timestamp = auditoria.getFecha();
				switch(auditoria.getAccion()) {
				case CAMBIOIMAGEN:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " modificó la imagen del grupo.");
					break;
				case CAMBIOMONEDA:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " modificó la moneda del grupo a " + detalle + ".");		
					break;
				case CAMBIOTIPOGRUPO:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " modificó el tipo de grupo a " + detalle + ".");	
					break;
				case NUEVACATEGORIAGRUPO:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " creó la categoría " + detalle + " en el grupo.");	
					break;
				case ELIMINACATEGORIAGRUPO:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " eliminó la categoría " + detalle + " del grupo.");
					break;
				case NUEVOPAGO:
					String nombreAvatar2 =  "Usuario desconocido";
					List<Avatar> avataresGrupo = grupoObj.getAvataresDeGrupo();
					for (Avatar avatarGrupo : avataresGrupo) {
						if(avatarGrupo.getId() == idEntidad) {
							nombreAvatar2 = avatarGrupo.getNombre();
							break;
						}						
					}
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " pagó " + detalle + " a " + nombreAvatar2);					
					break;
				case NUEVOGASTO:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " agregó un nuevo gasto con " + detalle + " en el grupo.");	
					break;
				case NUEVOAVATAR:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " agregó a " + detalle + " al grupo.");
					break;
				case NUEVAINVITACION:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " invitó a " + detalle + " al grupo.");
					break;
				case NUEVOGRUPO:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " creó el grupo.");
					break;
				case ACEPTARINVITACION:
					dtAuditoria.setDescripcion("El usuario " + nombreAvatar + " se unio al grupo con el email " + detalle + ".");
					break;
				}
				listaDtAuditoria.add(dtAuditoria);
			}
		}
		
		Collections.sort(listaDtAuditoria);
		return listaDtAuditoria;
	}

	@Override
	public List<DtAuditoriaUsuario> getAuditoriaUsuario(int usuarioId) throws UsuarioNoExisteException {
		List<DtAuditoriaUsuario> DTAuditoriaUsuarioList = new ArrayList<DtAuditoriaUsuario>();
		
		//Obtengo los avatares del usuario
		Optional<Usuario> usuarioOPT = usuarioRepo.findById(usuarioId);
		if (usuarioOPT.isPresent()) {
			UsuarioFinal usuario = (UsuarioFinal) usuarioOPT.get();
			
			DtAuditoriaUsuario DTAuditoriaUsuario = new DtAuditoriaUsuario("El usuario se unio al sistema.", usuario.getFechaAlta(), AuditoriaAccion.CREACIONUSUARIO);
			DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
			List<Avatar> avataresDelUsuario = usuario.getAvatares();
			
			//Para cada Avatar del usuario
			List<AuditoriaGrupo> listaAuditoriasUsuario;
			for (Avatar avatarAct : avataresDelUsuario) {
				
				//Obtengo las auditorias donde participa como Avatar, las recorro y agrego al retorno
				listaAuditoriasUsuario = auditoriaGrupoRepository.findAllByAvatarId(avatarAct.getId());
				
				for (AuditoriaGrupo auditoriaAct : listaAuditoriasUsuario) {
					
					DTAuditoriaUsuario = new DtAuditoriaUsuario();
					DTAuditoriaUsuario.setFecha(auditoriaAct.getFecha());
					DTAuditoriaUsuario.setAccion(auditoriaAct.getAccion());
					
					switch(auditoriaAct.getAccion()) {
						case NUEVOPAGO: 
							DTAuditoriaUsuario.setDescripcion("El usuario realizo un pago por " + auditoriaAct.getDetalle());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
						
						case NUEVOGASTO:
							GastoCompartido gasto = gastoRepo.findById(auditoriaAct.getIdEntidad()).get();
							DTAuditoriaUsuario.setDescripcion("El usuario ingreso el gasto " + gasto.getDescripcion() + 
									" por " + auditoriaAct.getDetalle());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
						
						case NUEVOAVATAR:
							DTAuditoriaUsuario.setDescripcion("El usuario creo el participante " + auditoriaAct.getDetalle() + 
									" en el grupo " + auditoriaAct.getGrupo().getNombre());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
							
						case NUEVAINVITACION:
							DTAuditoriaUsuario.setDescripcion("El usuario envio una invitacion para participar del grupo " + 
									auditoriaAct.getGrupo().getNombre() + " al mail " + auditoriaAct.getDetalle());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
							
						case NUEVOGRUPO:
							DTAuditoriaUsuario.setDescripcion("El usuario creo el grupo " + auditoriaAct.getGrupo().getNombre());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
							
						case ACEPTARINVITACION:
							DTAuditoriaUsuario.setDescripcion("El usuario se unio al grupo " + auditoriaAct.getGrupo().getNombre() + 
									" usando la invitacion enviada al email " + auditoriaAct.getDetalle());
							DTAuditoriaUsuarioList.add(DTAuditoriaUsuario);
							break;
					}
				}	
				
				//Obtengo las auditorias en las que participa como idEntidad (Solo NUEVO PAGO), las recorro y agrego al retorno
				listaAuditoriasUsuario = auditoriaGrupoRepository.findAllByIdEntidad(avatarAct.getId());
				for (AuditoriaGrupo auditoriaActual : listaAuditoriasUsuario) {
					
					DTAuditoriaUsuario = new DtAuditoriaUsuario();
					DTAuditoriaUsuario.setFecha(auditoriaActual.getFecha());
					DTAuditoriaUsuario.setAccion(auditoriaActual.getAccion());
					
					switch(auditoriaActual.getAccion()) {
						case NUEVOPAGO:
							DTAuditoriaUsuario.setDescripcion("El usuario recibio un pago de " + auditoriaActual.getAvatar() + 
									" por " + auditoriaActual.getDetalle());
							break;
					}
				}
			}
			
			Collections.sort(DTAuditoriaUsuarioList);
			return DTAuditoriaUsuarioList;
		}
		else {
			throw new UsuarioNoExisteException("El usuario no existe");
		}
	}
	public List<GruposPorDia> getGruposPorDia() {
		return auditoriaGrupoRepository.getGruposPorDia();
	}

	@Override
	public List<TransaccionesPorDia> getTransaccionsPorDia() {
		return auditoriaGrupoRepository.getTransaccionesPorDia();
	}
	
	public List<DtEstadisticasUsuario> getEstadisticasUsuario(){
		List<DtEstadisticasUsuario> estadisticas =  new ArrayList<DtEstadisticasUsuario>();
		Usuario usuario = usuarioRepo.findByEmail(jwtUtil.getEmail());
		if (usuario instanceof UsuarioFinal) {
			List<Avatar> listaAvatares = ((UsuarioFinal) usuario).getAvatares();
			for(Avatar avatar : listaAvatares) {
				GrupoDeGastos grupo = avatar.getGrupoGastos();
				DtEstadisticasUsuario dtEstadistica = new DtEstadisticasUsuario(grupo.getId(), grupo.getNombre(), grupo.getMoneda());
				
				AvatarMayorDeudor mayorDeudor = auditoriaGrupoRepository.getAvatarMayorDeudor(grupo.getId());
				dtEstadistica.setAvatarConMasDeuda(mayorDeudor);
				
				List<GastoPorCategoriaUsuario> gastoPorCategoria = auditoriaGrupoRepository.getGastosPorCategoria(grupo.getId(), avatar.getId());
				dtEstadistica.setCategorias(gastoPorCategoria);
				
				List<GastoPorMesUsuario> gastosPorMes = auditoriaGrupoRepository.getTotalGastosPorMes(grupo.getId(), avatar.getId());
				dtEstadistica.setTotalGastosPorMes(gastosPorMes);
				
				dtEstadistica.setTotal(auditoriaGrupoRepository.getTotalUsuarioPorGrupo(grupo.getId(), avatar.getId()));
				
				estadisticas.add(dtEstadistica);				
			}			
		}		
	
		
		return estadisticas;
		
	}

}
