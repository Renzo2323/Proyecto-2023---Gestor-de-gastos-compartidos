package com.splitit.splitit.backend.ServicesImplementations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Deuda;
import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.NotificacionUsuario;
import com.splitit.splitit.backend.DAO.Pago;
import com.splitit.splitit.backend.DAO.TipoMoneda;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.DtAvatar;
import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.DTO.DtPago;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.DTO.DtSaldoSimplificado;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExistePagoException;
import com.splitit.splitit.backend.Notifications.OneSignal;
import com.splitit.splitit.backend.Notifications.TipoInternoNotificacion;
import com.splitit.splitit.backend.Repository.AvatarRepository;
import com.splitit.splitit.backend.Repository.GrupoRepository;
import com.splitit.splitit.backend.Repository.NotificacionUsuarioRepository;
import com.splitit.splitit.backend.Repository.PagoRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.EmailService;
import com.splitit.splitit.backend.Services.PagoService;
import com.splitit.splitit.backend.Services.SimplificarDeudasService;
import com.splitit.splitit.backend.ServicesImplementations.SimplificarDeudasServiceImplementation.NetworkFlowSolverBase.Edge;
import com.splitit.splitit.backend.WebSocket.NotificacionController;
import com.splitit.splitit.backend.Services.AuditoriaService;

@Service
public class PagoServiceImplementation implements PagoService{

	@Autowired
	private GrupoRepository grupoGastosRepo;
	
	@Autowired
    private AvatarRepository avatarRepository;
	
	@Autowired
	private PagoRepository pagoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private SimplificarDeudasService simplificarDeudas;
	
	private AuditoriaService auditoriaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	public PagoServiceImplementation(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}
	
	@Autowired
	private NotificacionController notificacionController;
	
	@Autowired
	private NotificacionUsuarioRepository notiUsuRepo;
	
	@Override
	public DtResponse nuevoPago(DtPago dtPago) {
		GrupoDeGastos grupoGastos = grupoGastosRepo.findById(
				dtPago.getGrupoId()).get();
		
		Avatar pagador = avatarRepository.getById(dtPago.getAvatarPagador());
		if(!avatarPerteneceAGrupo(grupoGastos, pagador.getId())) {
			return new DtResponse("El avatar " + pagador.getId() + " no pertenece al grupo." , true);
		}
		
		Avatar receptor = avatarRepository.getById(dtPago.getAvatarReceptor());
		if(!avatarPerteneceAGrupo(grupoGastos, receptor.getId())) {
			return new DtResponse("El avatar " + receptor.getId() + " no pertenece al grupo." , true);
		}
		
		
		Pago pago = new Pago(dtPago.getMonto(), dtPago.getMoneda(), dtPago.getMetodoPago(), dtPago.getNota(), grupoGastos, pagador, receptor);
		pagoRepository.save(pago);
		
		
		pagador.setSaldoLocal(pagador.getSaldoLocal() + dtPago.getMonto());
		receptor.setSaldoLocal(receptor.getSaldoLocal() - dtPago.getMonto());
		if(esIgualConTolerancia(pagador.getSaldoLocal(), 0, 0.01f)) {
			pagador.setSaldoLocal(0);
		}
		if(esIgualConTolerancia(receptor.getSaldoLocal(), 0, 0.01f)) {
			receptor.setSaldoLocal(0);
		}
		avatarRepository.save(pagador);
		avatarRepository.save(receptor);
		
		//SE AGREGA AUDITORIA
		String detalle = "" + grupoGastos.getMonedaSimbolo() + dtPago.getMonto();
		auditoriaService.addAuditoriaGrupo(grupoGastos.getId(), AuditoriaAccion.NUEVOPAGO, pagador, receptor.getId(), detalle);
		try {
			Usuario usrPagador = pagador.getUsuario();
			Usuario usrReceptor = receptor.getUsuario();
			//Luego tambien agregar las notificaciones a un array del usuario
			if(usrPagador != null && !avatarEsUsuarioActual(pagador)) {
				String mensajePagador = "Pagaste " + detalle + " a " + receptor.getNombre();
				NotificacionUsuario notificacionUsuario = new NotificacionUsuario(LocalDateTime.now(), mensajePagador, false, (UsuarioFinal) usrPagador, grupoGastos);
				
				//Se guarda en la BD
				notiUsuRepo.save(notificacionUsuario);
				//Se envia por WebSocket
				notificacionController.enviarUsuario(mensajePagador, usrPagador.getId());
				//Se envia por Mail
				emailService.sendNotificacionPagoEnviado(usrPagador.getEmail(), mensajePagador);
				//Se envia por OneSignal
				enviarNotificacionPush(TipoInternoNotificacion.PAGOENVIADO, Arrays.asList(usrPagador.getEmail()), mensajePagador);				
			}
			
			if(usrReceptor != null && !avatarEsUsuarioActual(receptor)) {
				String mensajeReceptor = "Recibiste " + detalle + " de " + pagador.getNombre();
				NotificacionUsuario notificacionUsuario = new NotificacionUsuario(LocalDateTime.now(), mensajeReceptor, false, (UsuarioFinal) usrReceptor, grupoGastos);
				
				//Se guarda en la BD
				notiUsuRepo.save(notificacionUsuario);
				//Se envia por WebSocket
				notificacionController.enviarUsuario(mensajeReceptor, usrReceptor.getId());
				//Se envia por Mail
				emailService.sendNotificacionPagoRecibido(usrReceptor.getEmail(), mensajeReceptor);
				//Se envia por OneSignal
				enviarNotificacionPush(TipoInternoNotificacion.PAGORECIBIDO, Arrays.asList(usrReceptor.getEmail()), mensajeReceptor);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DtResponse("Pago creado con exito", true);
	}
	
	private void enviarNotificacionPush(TipoInternoNotificacion tipo, List<String> emails, String titulo) {
		OneSignal notificacion = new OneSignal();
		notificacion.createNotification(tipo, emails, titulo);
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
	public List<DtSaldo> getSaldosByGrupo(int grupoId) {
		GrupoDeGastos grupoGastos = grupoGastosRepo.findById(grupoId).get();
		List<DtSaldo> saldos = new ArrayList<DtSaldo>();
		
		
		List<GastoCompartido> gastos = grupoGastos.getGastosCompartidos();
		
		boolean existeSaldo;
		for(GastoCompartido gasto : gastos) {
			List<Deuda> deudas =  gasto.getDeudas();
			TipoMoneda monedaGasto = gasto.getMoneda(); 
			
			for(Deuda deuda : deudas) {
				if(deuda.getAcreedor().getId() != deuda.getDeudor().getId()) {
					if(saldos.isEmpty()) {
						Avatar avatarDeudor = deuda.getDeudor();
						Avatar avatarAcreedor = deuda.getAcreedor();
						DtSaldo dtSaldo = crearSaldo(avatarDeudor, avatarAcreedor, deuda.getMonto(), monedaGasto);
						saldos.add(dtSaldo);
						
					}else {
						existeSaldo = false;
						for(DtSaldo saldo : saldos) {
							//Mismo deudor, mismo acreedor y misma moneda 
							if(deuda.getDeudor().getId() == saldo.getDeudor().getId() && deuda.getAcreedor().getId() == saldo.getAcreedor().getId() && saldo.getMoneda() == monedaGasto) {
								existeSaldo = true;
								saldo.setMonto(saldo.getMonto() + deuda.getMonto());
								break;
							}
						}
						if(!existeSaldo) {
							Avatar avatarDeudor = deuda.getDeudor();
							Avatar avatarAcreedor = deuda.getAcreedor();
							DtSaldo dtSaldo = crearSaldo(avatarDeudor, avatarAcreedor, deuda.getMonto(), monedaGasto);
							saldos.add(dtSaldo);
						}
						
					}
				}
			}			
		}	

		List<Pago> pagos = grupoGastos.getPagosGrupo();
		
		
		for(Pago pago : pagos) {
			existeSaldo = false;
			ListIterator<DtSaldo> iter = saldos.listIterator();
			while(iter.hasNext()) {
				DtSaldo saldo = iter.next();
				//Mismo deudor, mismo acreedor y misma moneda 
				if(pago.getPagador().getId() == saldo.getDeudor().getId() && pago.getReceptor().getId() == saldo.getAcreedor().getId() && saldo.getMoneda() == pago.getMoneda()) {
					existeSaldo = true;
					float montoRestante = saldo.getMonto() - pago.getMonto();
					if(montoRestante == 0) {
						iter.remove();
					}else if(montoRestante > 0) {
						saldo.setMonto(montoRestante);
					}else {
						DtAvatar acreedor = saldo.getAcreedor();
						DtAvatar deudor = saldo.getDeudor();
						saldo.setDeudor(acreedor);
						saldo.setAcreedor(deudor);
						saldo.setMonto(montoRestante * -1);
					}
					
					break;
				}
			}
			if (!existeSaldo) {
				Avatar avatarDeudor = pago.getReceptor();
				Avatar avatarAcreedor = pago.getPagador();
				DtSaldo dtSaldo = crearSaldo(avatarDeudor, avatarAcreedor, pago.getMonto(), pago.getMoneda());
				saldos.add(dtSaldo);
				
			}
		}
		
		return saldos;
	}
	
	
	@Override
	public List<DtSaldo> getSaldosSimplificadosByGrupo(int grupoId) {
		List<DtSaldoSimplificado> saldosSimplificados = new ArrayList<DtSaldoSimplificado>();
		List<DtSaldo> saldosFinales = new ArrayList<DtSaldo>();

		List<DtSaldo> saldos = getSaldosByGrupo(grupoId);
		GrupoDeGastos grupo = grupoGastosRepo.getById(grupoId);
		
		

		saldosSimplificados = simplificarDeudas.simplificarDeudas(saldos);
		
		for(DtSaldoSimplificado dtSaldoSim : saldosSimplificados) {
			Avatar avatarDeudor = avatarRepository.getById(dtSaldoSim.getDeudor());
			DtAvatar dtAvatarDeudor = getDtAvatar(avatarDeudor);
			
			Avatar avatarAcreedor = avatarRepository.getById(dtSaldoSim.getAcreedor());
			DtAvatar dtAvatarAcreedor = getDtAvatar(avatarAcreedor);
			
			
			DtSaldo dtSaldo = new DtSaldo(dtAvatarDeudor, dtAvatarAcreedor, (float)dtSaldoSim.getMonto(), grupo.getMoneda());
			saldosFinales.add(dtSaldo);
		}			


		return saldosFinales;
	}
	
	
	private DtAvatar getDtAvatar(Avatar avatar) {
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		boolean esUsuarioActual = false;
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
		
		return dtAvatar;
	}
	
	private DtSaldo crearSaldo(Avatar avatarDeudor, Avatar avatarAcreedor, float monto, TipoMoneda moneda) {
		DtAvatar dtAvatarDeudor = getDtAvatar(avatarDeudor);
		DtAvatar dtAvatarAcreedor = getDtAvatar(avatarAcreedor);
		
		DtSaldo dtSaldo = new DtSaldo(dtAvatarDeudor, dtAvatarAcreedor, monto, moneda);
		
		return dtSaldo;
	}


	@Override
	public List<DtSaldo> getSaldosSimplificadosByGrupoNew(int grupoId) throws NoExisteGrupoException, Exception{
		List<DtSaldo> saldosFinales = new ArrayList<DtSaldo>();
		
		GrupoDeGastos grupo = grupoGastosRepo.findById(grupoId).orElseThrow(NoExisteGrupoException::new);
		List<Avatar> avataresDeGrupo = grupo.getAvataresDeGrupo();
		
		List<DtAvatar> deudores = new ArrayList<DtAvatar>();
		List<DtAvatar> acreedores = new ArrayList<DtAvatar>();
		for(Avatar avatar : avataresDeGrupo) {
			if(avatar.getSaldoLocal() > 0) { //OBTENGO ACREEDORES
				DtAvatar dtAvatar = new DtAvatar();
				if(avatarEsUsuarioActual(avatar)) {//Usuario conectado
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), true, true);
				}else {
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), false, false);
				}
				
				acreedores.add(dtAvatar);
			}else if (avatar.getSaldoLocal() < 0) { //OBTENGO DEUDORES
				DtAvatar dtAvatar = new DtAvatar();
				if(avatarEsUsuarioActual(avatar)) {//Usuario conectado
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), true, true);
				}else {
					dtAvatar = new DtAvatar(avatar.getId(), avatar.getNombre(), avatar.getSaldoLocal(), false, false);
				}
				deudores.add(dtAvatar);
			}			
		}
		
		deudores.sort(Comparator.comparing(DtAvatar::getSaldoLocal));
		
		acreedores.sort(Comparator.comparing(DtAvatar::getSaldoLocal));
		Collections.reverse(acreedores);
		
		/*
		System.out.println("ACREEDORES");
		for(DtAvatar acreedor : acreedores) {
			System.out.println(acreedor.getNombre() + " - Saldo: " + acreedor.getSaldoLocal());  
		}
		System.out.println("-----------");
		System.out.println("DEUDORES");
		for(DtAvatar deudor : deudores) {
			System.out.println(deudor.getNombre() + " - Saldo: " + deudor.getSaldoLocal());  
		}*/

		for(DtAvatar acreedor : acreedores) {
			int contador = 0;
			while(acreedor.getSaldoLocal() > 0) {
				contador ++;
				if(contador > 40) {
					throw new Exception("Demoró demasiado");
				}
				//CHEQUEO SALDOS IGUALES
				for (DtAvatar deudor : deudores) {
					if(esIgualConTolerancia(acreedor.getSaldoLocal(), (deudor.getSaldoLocal() * -1), 0.01f) && !esIgualConTolerancia(acreedor.getSaldoLocal(), 0, 0.01f)) {
						
						DtSaldo dtSaldo = new DtSaldo(deudor, acreedor, acreedor.getSaldoLocal(), grupo.getMoneda());
						saldosFinales.add(dtSaldo);
						
						acreedor.setSaldoLocal(0);
						deudor.setSaldoLocal(0);

						break;
					}				
				}
				
				//SI SIGUE CON SALDO PENDIENTE
				if(!esIgualConTolerancia(acreedor.getSaldoLocal(), 0, 0.01f)) {
					for (DtAvatar deudor : deudores) {
						
						if(deudor.getSaldoLocal() < 0 && (deudor.getSaldoLocal() * -1) <= acreedor.getSaldoLocal()) { //CREO que el igual aqui no es necesario porque arriba ya se fija si son iguales
							DtSaldo dtSaldo = new DtSaldo(deudor, acreedor, (deudor.getSaldoLocal() * -1), grupo.getMoneda());
							saldosFinales.add(dtSaldo);
							
							acreedor.setSaldoLocal(acreedor.getSaldoLocal() + deudor.getSaldoLocal());
							if(esIgualConTolerancia(acreedor.getSaldoLocal(), 0, 0.01f)) {
								acreedor.setSaldoLocal(0);
							}
							deudor.setSaldoLocal(0);

							break;
						}else if(deudor.getSaldoLocal() < 0){
						
							DtSaldo dtSaldo = new DtSaldo(deudor, acreedor, acreedor.getSaldoLocal(), grupo.getMoneda());
							saldosFinales.add(dtSaldo);
							
							deudor.setSaldoLocal(deudor.getSaldoLocal() + acreedor.getSaldoLocal());
							if(esIgualConTolerancia(deudor.getSaldoLocal(), 0, 0.01f)) {
								deudor.setSaldoLocal(0);
							}
							acreedor.setSaldoLocal(0);
							
							break;
						}
					}
				}else {
					acreedor.setSaldoLocal(0); //Talves no necesario
				}
			}			
		}
		/*
		System.out.println("-----------------------------------------------------------------");
		System.out.println("ACREEDORES");
		for(DtAvatar acreedor : acreedores) {
			System.out.println(acreedor.getNombre() + " - Saldo: " + acreedor.getSaldoLocal());  
		}
		
		System.out.println("DEUDORES");
		for(DtAvatar deudor : deudores) {
			System.out.println(deudor.getNombre() + " - Saldo: " + deudor.getSaldoLocal());  
		}
		*/
		return saldosFinales;
	}
	
	
	private boolean avatarEsUsuarioActual(Avatar avatar) {
		Usuario usuarioActual = usuarioRepository.findByEmail(jwtUtil.getEmail());
		Usuario usuarioAvatar = avatar.getUsuario();
		if(usuarioActual != null && usuarioAvatar != null && usuarioAvatar.getId() == usuarioActual.getId()) {
			return true;
		}
		
		return false;
	}
	
	private boolean esIgualConTolerancia(float x, float y, float tolerancia) {
		return Math.abs(x - y) < tolerancia;
	}

	@Override
	public DtResponse eliminarPago(int pagoId)  throws NoExistePagoException{
		Pago pago = pagoRepository.findById(pagoId).orElseThrow(NoExistePagoException::new);
		Avatar pagador = pago.getPagador();
		Avatar receptor = pago.getReceptor();
		
		pagador.setSaldoLocal(pagador.getSaldoLocal() - pago.getMonto());
		receptor.setSaldoLocal(receptor.getSaldoLocal() + pago.getMonto());
		
		pagoRepository.delete(pago);
		
		return new DtResponse("Pago eliminado con exito", true);
	}
}

