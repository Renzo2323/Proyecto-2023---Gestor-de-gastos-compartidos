package com.splitit.splitit.backend.ServicesImplementations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import jakarta.transaction.Transactional;

import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Categoria;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.NotificacionUsuario;
import com.splitit.splitit.backend.DAO.Reset_Token;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioAdministrador;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.DtLogin;
import com.splitit.splitit.backend.DTO.DtNotificacionUsuario;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtResponseLogin;
import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.DTO.DtUsuario;
import com.splitit.splitit.backend.DTO.DtUsuarioAdministrador;
import com.splitit.splitit.backend.DTO.DtUsuarioFinal;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.Exceptions.ExisteUsuarioException;
import com.splitit.splitit.backend.Exceptions.InvitacionInvalidaException;
import com.splitit.splitit.backend.Exceptions.MailingException;
import com.splitit.splitit.backend.Exceptions.NoExisteNotificacion;
import com.splitit.splitit.backend.Exceptions.TokenVencidoException;
import com.splitit.splitit.backend.Exceptions.UsuarioBloqueadoException;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;
import com.splitit.splitit.backend.Notifications.OneSignal;
import com.splitit.splitit.backend.Repository.AvatarRepository;
import com.splitit.splitit.backend.Repository.NotificacionUsuarioRepository;
import com.splitit.splitit.backend.Repository.ParametroRepository;
import com.splitit.splitit.backend.Repository.TokenRepository;
import com.splitit.splitit.backend.Repository.UsuarioFinalRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.EmailService;
import com.splitit.splitit.backend.Services.GrupoService;
import com.splitit.splitit.backend.Services.UsuarioService;


@Service
public class UsuarioServiceImplementation implements UsuarioService{
	
	@Autowired
	private UsuarioFinalRepository usuarioFinalRepository;
	
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
	private TokenRepository tokenRepository;
    
    @Autowired
	private AvatarRepository avatarRepository;
    
    @Autowired
    private ParametroRepository parametroRepository;
    
    @Autowired
	private EmailService emailService;
    
    @Autowired
    private GrupoService grupoService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private AuditoriaService auditoriaService;
	
    @Autowired
    private NotificacionUsuarioRepository notificacioRepository;
    
	@Autowired
	public UsuarioServiceImplementation(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}
    
    @Override
    public DtResponse addUsuarioFinal(DtUsuarioFinal dtUsuarioFinal) throws ExisteUsuarioException{
    	DtResponse respuesta =  new DtResponse("", false);
    	respuesta = validarDatosUsuario(dtUsuarioFinal);
    	if(!respuesta.getEstatus()) throw new ExisteUsuarioException(respuesta.getMensaje());
    	if(usuarioRepository.findByEmail(dtUsuarioFinal.getEmail()) != null)throw new ExisteUsuarioException("Usuario con email " + dtUsuarioFinal.getEmail() + " ya se encuentra en el sistema.");
    	LocalDateTime fechaActual = LocalDateTime.now();
    	UsuarioFinal aux_Usuario = new UsuarioFinal(
	        dtUsuarioFinal.getNombre(), 
	        dtUsuarioFinal.getApellido(),
	        dtUsuarioFinal.getEmail(), 
	        dtUsuarioFinal.getFechaNacimiento(),
	        fechaActual, 
	        this.passwordEncoder.encode(dtUsuarioFinal.getContrasena()), 
	        dtUsuarioFinal.getImagen(), 
	        null, 
	        0, 
	        0, 
	        0, 
	        false, 
	        null);
    	
        usuarioRepository.save(aux_Usuario);
        String nombreApellido = dtUsuarioFinal.getNombre() + " " + dtUsuarioFinal.getApellido();
        jwtUtil.generateToken(dtUsuarioFinal.getEmail(), dtUsuarioFinal.getNombre(), false);
        
        OneSignal notificaciones = new OneSignal();
        notificaciones.createUser(dtUsuarioFinal.getEmail());
        
        respuesta = new DtResponse("Usuario creado con exito",true);
        return respuesta;	
    }
    		
	
   @Override
   public ResponseEntity<?> loginUsuario(DtLogin dtLogin) {
        Usuario usuario1 = (Usuario) usuarioRepository.findByEmail(dtLogin.getEmail());
        if (usuario1 != null) {
            String contrasena = dtLogin.getContrasena();
            String encodedPassword  = usuario1.getContrasena();
            Boolean isPwdRight = passwordEncoder.matches(contrasena, encodedPassword );
            if (isPwdRight) {
                Optional<Usuario> user = usuarioRepository.findOneByEmailAndContrasena(dtLogin.getEmail(), encodedPassword );
                if (user.isPresent()) {
                	DtUsuario dtUsuarioAct = null;
                	Boolean admin = false;
        			if(usuario1 instanceof UsuarioFinal) {
        				if(((UsuarioFinal)usuario1).isBloqueado()) {
        					return ResponseEntity.ok(new DtResponse("El usuario está bloqueado", false));
        				}
        					dtUsuarioAct = new  DtUsuarioFinal(usuario1.getId(), usuario1.getNombre(), usuario1.getApellido(), usuario1.getEmail(), usuario1.getFechaNacimiento(), usuario1.getFechaAlta(), usuario1.getContrasena(), usuario1.getImagen(), ((UsuarioFinal)usuario1).getDebitoTotal(), ((UsuarioFinal)usuario1).getCreditoTotal(), ((UsuarioFinal)usuario1).getSaldoTotal(), ((UsuarioFinal)usuario1).isBloqueado());
        					admin = false;
        						
        				}else if(usuario1 instanceof UsuarioAdministrador){
        					dtUsuarioAct = new DtUsuarioAdministrador(usuario1.getId(), usuario1.getNombre(), usuario1.getApellido(), usuario1.getEmail(), usuario1.getFechaNacimiento(), usuario1.getFechaAlta(), usuario1.getContrasena(), usuario1.getImagen());
        					admin = true;
        			}
        			String token = jwtUtil.generateToken(dtLogin.getEmail(), dtUsuarioAct.getNombre(), admin);
                    return ResponseEntity.ok(new DtResponseLogin("Login Exitoso",token,true));
                } else {
                    return ResponseEntity.ok(new DtResponse("Login Fallido", false));
                }
            } else {
                return ResponseEntity.ok(new DtResponse("Contraseña incorrecta", false));
            }
        }else {
            return ResponseEntity.ok(new DtResponse("No existe una cuenta asociada a ese correo", false));
        }
    }
   
   
   @Override
	public ResponseEntity<?> loginApp(DtLogin dtLogin) {
	   String msg = "";
       Usuario usuario1 = usuarioRepository.findByEmail(dtLogin.getEmail());
       if (usuario1 != null) {
    	   if(usuario1 instanceof UsuarioFinal) {
	           String contrasena = dtLogin.getContrasena();
	           String encodedPassword  = usuario1.getContrasena();
	           Boolean isPwdRight = passwordEncoder.matches(contrasena, encodedPassword );
	           if (isPwdRight) {
	               Optional<Usuario> employee = usuarioRepository.findOneByEmailAndContrasena(dtLogin.getEmail(), encodedPassword );
	               if (employee.isPresent()) {
	               	DtUsuario dtUsuarioAct = null;
	       			if(usuario1 instanceof UsuarioFinal) {
	       				if(((UsuarioFinal)usuario1).isBloqueado()) {
	       					return ResponseEntity.ok(new DtResponse("El usuario está bloqueado", false));
	       				}
	       				dtUsuarioAct = new DtUsuarioFinal(usuario1.getId(), usuario1.getNombre(), usuario1.getApellido(), usuario1.getEmail(), usuario1.getFechaNacimiento(), usuario1.getFechaAlta(), usuario1.getContrasena(), usuario1.getImagen(), ((UsuarioFinal)usuario1).getDebitoTotal(), ((UsuarioFinal)usuario1).getCreditoTotal(), ((UsuarioFinal)usuario1).getSaldoTotal(), ((UsuarioFinal)usuario1).isBloqueado());
	       			}else if(usuario1 instanceof UsuarioAdministrador){
	       				dtUsuarioAct = new DtUsuarioAdministrador(usuario1.getId(), usuario1.getNombre(), usuario1.getApellido(), usuario1.getEmail(), usuario1.getFechaNacimiento(), usuario1.getFechaAlta(), usuario1.getContrasena(), usuario1.getImagen());
	       			}
	       			String token = jwtUtil.generateToken(dtLogin.getEmail(), dtUsuarioAct.getNombre(), false);
                    return ResponseEntity.ok(new DtResponseLogin("Login Exitoso",token,true));
	               } else {
	                   return ResponseEntity.ok(new DtResponse("Login Fallido", false));
	               }
	           } else {
	               return ResponseEntity.ok(new DtResponse("Contraseña incorrecta", false));
	           }
    	   }else {
    		   return ResponseEntity.ok(new DtResponse("Usuario administrador debe loguearse via web", false));
    	   }
       }else {
           return ResponseEntity.ok(new DtResponse("No existe una cuenta asociada a ese correo", false));
       }
	}
   
   

   @Override
   public List<DtUsuario> getUsuarios() {
		//Voy a buscar los usuarios a la base
		List<Usuario> listaUsuarios = usuarioRepository.findAll();
		
		//Creo e inicializo la lista a devolver
		List<DtUsuario> listaDTUsuario = new ArrayList<DtUsuario>();
		
		//Por cada Usuario creo el DtUsuario correspondiente y lo agrego a la lista
		for (Usuario usuarioActual : listaUsuarios) {
			DtUsuario dtUsuarioAct = null;
			if(usuarioActual instanceof UsuarioFinal) {
				dtUsuarioAct = new DtUsuarioFinal(usuarioActual.getId(), usuarioActual.getNombre(), usuarioActual.getApellido(),  usuarioActual.getEmail(), usuarioActual.getFechaNacimiento(), usuarioActual.getFechaAlta(), usuarioActual.getContrasena(), usuarioActual.getImagen(), ((UsuarioFinal)usuarioActual).getDebitoTotal(), ((UsuarioFinal)usuarioActual).getCreditoTotal(), ((UsuarioFinal)usuarioActual).getSaldoTotal(), ((UsuarioFinal)usuarioActual).isBloqueado());
			}else if(usuarioActual instanceof UsuarioAdministrador){
				dtUsuarioAct = new DtUsuarioAdministrador(usuarioActual.getId(), usuarioActual.getNombre(), usuarioActual.getApellido(), usuarioActual.getEmail(), usuarioActual.getFechaNacimiento(), usuarioActual.getFechaAlta(), usuarioActual.getContrasena(), usuarioActual.getImagen());
			}
			//DtUsuario dtUsuarioAct = new DtUsuario(usuarioActual.getId(), usuarioActual.getNombre(), usuarioActual.getEmail(), usuarioActual.getFechaAlta(), usuarioActual.getContrasena(), usuarioActual.getImagen(), usuarioActual.getToken());
			listaDTUsuario.add(dtUsuarioAct);
		}
		
		return listaDTUsuario;
	}
   
   public ResponseEntity<?> getUsuario() {
	   String email = jwtUtil.getEmail();
	   Usuario usuarioObj = usuarioRepository.findByEmail(email);
	   if(usuarioObj != null) {
			DtUsuario dtUsuario = null;
			if(usuarioObj instanceof UsuarioAdministrador) {
				dtUsuario = new DtUsuarioAdministrador(usuarioObj.getId(), usuarioObj.getNombre(), usuarioObj.getApellido(), usuarioObj.getEmail(), usuarioObj.getFechaNacimiento(), usuarioObj.getFechaAlta(), usuarioObj.getContrasena(), usuarioObj.getImagen());
			}else if (usuarioObj instanceof UsuarioFinal) {
				dtUsuario = new DtUsuarioFinal(usuarioObj.getId(), usuarioObj.getNombre(), usuarioObj.getApellido(), usuarioObj.getEmail(), usuarioObj.getFechaNacimiento(), usuarioObj.getFechaAlta(), usuarioObj.getContrasena(), usuarioObj.getImagen(), ((UsuarioFinal)usuarioObj).getDebitoTotal(), ((UsuarioFinal)usuarioObj).getCreditoTotal(), ((UsuarioFinal)usuarioObj).getSaldoTotal(), ((UsuarioFinal)usuarioObj).isBloqueado());
			}
			return ResponseEntity.ok(dtUsuario);

		}else {
			return ResponseEntity.ok(new DtResponse("No se encuentra en el sistema", true));
		}
   }


	@Override
	public DtResponse addUsuarioAdministrador(DtUsuario dtUsuario) throws ExisteUsuarioException {
		DtResponse respuesta =  new DtResponse("", false);
    	respuesta = validarDatosUsuario(dtUsuario);
    	if(!respuesta.getEstatus()) throw new ExisteUsuarioException(respuesta.getMensaje());
		if(usuarioRepository.findByEmail(dtUsuario.getEmail()) != null)throw new ExisteUsuarioException("Usuario con email " + dtUsuario.getEmail() + " ya se encuentra en el sistema.");
		LocalDateTime fechaActual = LocalDateTime.now();
		UsuarioAdministrador aux_Usuario = new UsuarioAdministrador(
				dtUsuario.getNombre(), 
				dtUsuario.getApellido(),
				dtUsuario.getEmail(), 
				dtUsuario.getFechaNacimiento(),
				fechaActual, 
				this.passwordEncoder.encode(dtUsuario.getContrasena()), 
				dtUsuario.getImagen(), 
				null
		);
		
	    usuarioRepository.save(aux_Usuario);
	    
	    respuesta = new DtResponse("Usuario administrador creado con exito",true);
	    return respuesta;	
	}
	
	
	@Override
	public String generateResetToken(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		Reset_Token aux_token = (Reset_Token) tokenRepository.findByUsuario(usuario);
		//EL USUARIO PIDE UN TOKEN POR PRIMERA VEZ O EXPIRO
		if(aux_token == null) {
			String uuid = RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz") + "-" + RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz");
			LocalDateTime fechaActual = LocalDateTime.now();
			LocalDateTime fechaVencimiento = fechaActual.plusMinutes(1);
			Reset_Token resetToken = new Reset_Token();
			resetToken.setUsuario(usuario);
			resetToken.setToken(uuid);
			resetToken.setVencimiento(fechaVencimiento);
			resetToken.setUsuario(usuario);
			Reset_Token token = tokenRepository.save(resetToken);
			if (token != null) {
				String protocoloURL = parametroRepository.getByNombre("PROTOCOLO_FRONTEND").getValor();
				String hostURL = parametroRepository.getByNombre("HOST_FRONTEND").getValor();
				String puertoURL = parametroRepository.getByNombre("PUERTO_FRONTEND").getValor();
				String endpointUrl = protocoloURL + "://" + hostURL + ":" + puertoURL + "/RecuperarContrasena";
				return endpointUrl + "/" + resetToken.getToken();
			}
				return "";
		}else if(!tokenHasExipred(aux_token.getVencimiento())){
				String uuid = RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz") + "-" + RandomStringUtils.random(4, "0123456789abcdefghijklmnopqrstuvwxyz");
				LocalDateTime fechaActual = LocalDateTime.now();
				LocalDateTime fechaVencimiento = fechaActual.plusMinutes(1);
				aux_token.setUsuario(usuario);
				aux_token.setToken(uuid);
				aux_token.setVencimiento(fechaVencimiento);
				aux_token.setUsuario(usuario);
				Reset_Token token = tokenRepository.save(aux_token);
				if (token != null) {
					String protocoloURL = parametroRepository.getByNombre("PROTOCOLO_FRONTEND").getValor();
					String hostURL = parametroRepository.getByNombre("HOST_FRONTEND").getValor();
					String puertoURL = parametroRepository.getByNombre("PUERTO_FRONTEND").getValor();
					String endpointUrl = protocoloURL + "://" + hostURL + ":" + puertoURL + "/RecuperarContrasena";
					return endpointUrl + "/" + aux_token.getToken();
				}
					return "";
			}else {
				//EL USUARIO PIDIO UN TOKEN Y NO VENCIO
				Reset_Token resetToken = tokenRepository.findByUsuario(usuario);
				
				String protocoloURL = parametroRepository.getByNombre("PROTOCOLO_FRONTEND").getValor();
				String hostURL = parametroRepository.getByNombre("HOST_FRONTEND").getValor();
				String puertoURL = parametroRepository.getByNombre("PUERTO_FRONTEND").getValor();
				String endpointUrl = protocoloURL + "://" + hostURL + ":" + puertoURL + "/RecuperarContrasena";
				return endpointUrl + "/" + resetToken.getToken();
				}		
		
		
	}


	@Override
	public DtResponse resetPassword(String email) throws UsuarioNoExisteException, UsuarioBloqueadoException {
		Usuario usuario = usuarioRepository.findByEmail(email);
		if (usuario != null) {
			if (usuario instanceof UsuarioAdministrador) {
				String tokenURL = generateResetToken(email);
				try {
					emailService.sendResetToken(usuario, tokenURL);
				} catch (MailingException e) {
					e.printStackTrace();
				}	
				
			}else {
				if(!((UsuarioFinal) usuario).isBloqueado()) {
					String tokenURL = generateResetToken(email);
					try {
						emailService.sendResetToken(usuario, tokenURL);
					} catch (MailingException e) {
						e.printStackTrace();
					}	
				}else{
					throw new UsuarioBloqueadoException();
				}
			}
			
			
		}else {
			throw new UsuarioNoExisteException();
		}
		DtResponse respuesta = new DtResponse("Se envio el correo correctamente",true);
	    return respuesta;
	}


	@Override
	public boolean tokenHasExipred(LocalDateTime vencimiento) {
		LocalDateTime fechaActual = LocalDateTime.now();
		return vencimiento.isAfter(fechaActual);
	}


	@Override
	public DtResponse actualizarPassUsuario(DtUsuario dtUsuario) {
		Usuario aux_usuario = usuarioRepository.findByEmail(dtUsuario.getEmail());
		aux_usuario.setContrasena(this.passwordEncoder.encode(dtUsuario.getContrasena()));
		usuarioRepository.save(aux_usuario);
		DtResponse respuesta = new DtResponse("Usuario actualizado de manera correcta",true);
        return respuesta;
	}

	@Transactional
	@Override
	public DtResponse deleteUser(int usuarioId) throws UsuarioNoExisteException{
		DtResponse respuesta =  new DtResponse("", false);
		Usuario aux_usuario = usuarioRepository.findById(usuarioId).orElseThrow(UsuarioNoExisteException::new);
		
		if(aux_usuario instanceof UsuarioFinal) {
			List<Avatar> avatares = ((UsuarioFinal) aux_usuario).getAvatares();
			for(Avatar avatar : avatares) {
				avatar.setUsuario(null);
				avatarRepository.save(avatar);
				GrupoDeGastos grupo = avatar.getGrupoGastos();
				if(!grupo.tieneAvataresVinculados()) {
					try {
						DtResponse a = grupoService.deleteGrupo(grupo.getId());
					} catch (Exception e) {
						System.out.println(e);
					}
				}				
			}
			usuarioRepository.delete(aux_usuario);
			
		}else {
			usuarioRepository.delete(aux_usuario);
		}
		respuesta = new DtResponse("El usuario se elimino correctamente",true);

		return respuesta;
	}


	@Override
	public DtResponse editDatosUsuario(int usuarioId, DtUsuario dtUsuario) {
		DtResponse respuesta =  new DtResponse("", false);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		if(usuario.isPresent()) {
			Usuario usuarioObj = usuario.get();
			Usuario aux_usuario = usuarioRepository.findByEmail(dtUsuario.getEmail());
			if (aux_usuario != null && usuarioObj.getEmail() != aux_usuario.getEmail()) {
				respuesta = new DtResponse("Ya existe un usuario registrado con el email ingresado",false);
			}else {
				if (usuarioObj instanceof UsuarioFinal) {
					((UsuarioFinal)usuarioObj).setNombre(dtUsuario.getNombre());
					((UsuarioFinal)usuarioObj).setApellido(dtUsuario.getApellido());
					((UsuarioFinal)usuarioObj).setEmail(dtUsuario.getEmail());
					((UsuarioFinal)usuarioObj).setImagen(dtUsuario.getImagen());
					((UsuarioFinal)usuarioObj).setFechaNacimiento(dtUsuario.getFechaNacimiento());
					
					
					updateNombreAvatares((UsuarioFinal)usuarioObj, dtUsuario.getNombre());
					
				}
				if (usuarioObj instanceof UsuarioAdministrador) {
					((UsuarioAdministrador)usuarioObj).setNombre(dtUsuario.getNombre());
					((UsuarioAdministrador)usuarioObj).setApellido(dtUsuario.getApellido());
					((UsuarioAdministrador)usuarioObj).setEmail(dtUsuario.getEmail());
					((UsuarioAdministrador)usuarioObj).setImagen(dtUsuario.getImagen());
					((UsuarioAdministrador)usuarioObj).setFechaNacimiento(dtUsuario.getFechaNacimiento());
				}

				usuarioRepository.save(usuarioObj);
				respuesta = new DtResponse("El usuario se actualizo correctamente",true);
				
			}
		}else {
			respuesta = new DtResponse("El usuario no existe",false);
		}
		
		return respuesta;
	}
	
	@Override
	public DtResponse editMisDatosUsuario(DtUsuario dtUsuario) {
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		return editDatosUsuario(usuario.getId(), dtUsuario);
	}


	@Override
	public DtResponse darDeBajaUsuario(int usuarioId) {
		DtResponse respuesta =  new DtResponse("", false);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		if(usuario.isPresent()) {
			Usuario usuarioObj = usuario.get();
			if (usuarioObj instanceof UsuarioFinal) {
				((UsuarioFinal) usuarioObj).setBloqueado(true);
				usuarioRepository.save(usuarioObj);
			}
			respuesta = new DtResponse("El usuario se dio de baja correctamente",true);
		}else {
			respuesta = new DtResponse("No se pudo dar de baja el usuario",false);
		}
		return respuesta;
	}
	
	public DtResponse activarUsuario(int usuarioId) {
		DtResponse respuesta =  new DtResponse("", false);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		if(usuario.isPresent()) {
			Usuario usuarioObj = usuario.get();
			if (usuarioObj instanceof UsuarioFinal) {
				((UsuarioFinal) usuarioObj).setBloqueado(false);
				usuarioRepository.save(usuarioObj);
			}
			respuesta = new DtResponse("El usuario se activo correctamente",true);
		}else {
			respuesta = new DtResponse("No se pudo activar el usuario",false);
		}
		return respuesta;
	}
	
	private DtResponse validarDatosUsuario(DtUsuario dtUsuario) {
		DtResponse respuesta =  new DtResponse("", false);
		if(dtUsuario.getNombre().isEmpty()) {
			respuesta = new DtResponse("El nombre no puede ser vacio", false);
			return respuesta;
		}
		if(dtUsuario.getApellido().isEmpty()) {
			respuesta = new DtResponse("El apellido no puede ser vacio", false);
			return respuesta;
		}
		if(dtUsuario.getEmail().isEmpty()) {
			respuesta = new DtResponse("El correo no puede ser vacio", false);
			return respuesta;
		}
		if(dtUsuario.getContrasena().isEmpty()) {
			respuesta = new DtResponse("La contraseña no puede ser vacia", false);
			return respuesta;
		}
		
		respuesta = new DtResponse("Datos correctos", true);
		return respuesta;
	}
	
	@Override
	public DtResponse aceptarInvitacion(String tokenInvitacion) throws InvitacionInvalidaException{
		Optional<Avatar> avatarOpt = avatarRepository.findByTokenInvitacion(tokenInvitacion);
		
		if (avatarOpt.isPresent()) {
			Avatar avatar = avatarOpt.get();
			
			if (avatar.getUsuario() == null) {
				
				//Obtengo el usuario logueado con el Email
				Usuario usuario =  usuarioRepository.findByEmail(jwtUtil.getEmail());
				avatar.setUsuario((UsuarioFinal) usuario);
				avatar.setNombre(usuario.getNombre());
				avatarRepository.save(avatar);
				
				//SE AGREGA AUDITORIA
				auditoriaService.addAuditoriaGrupo(avatar.getIdGrupoDeGastos(), AuditoriaAccion.ACEPTARINVITACION, avatar, 0, usuario.getEmail());
//				
				
				return new DtResponse("La invitacion fue aceptada correctamente", true);
			}
			else {
				throw new InvitacionInvalidaException("Esta invitacion ya fue aceptada");
			}
		}
		else {
			throw new InvitacionInvalidaException("No se encontro avatar con el token " + tokenInvitacion);
		}
	}
	
	@Override
	public ResponseEntity<?> getAvatarEmail(int avatarId) {
		Usuario usuario = avatarRepository.findById(avatarId).get().getUsuario();
		if(usuario != null) {
			return ResponseEntity.ok(usuario.getEmail());
		}else {
			return ResponseEntity.ok(new DtResponse("No tiene un usuario relacionado", false));
		}
	}
	
	
	@Override
	public DtResponse cambiarPass(String oldPass, String newPass) {
		DtResponse respuesta = new DtResponse("", false);
	
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
        String encodedPassword  = usuario.getContrasena();
        Boolean isPwdRight = passwordEncoder.matches(oldPass, encodedPassword );
        if(isPwdRight) {
        	usuario.setContrasena(this.passwordEncoder.encode(newPass));
    		usuarioRepository.save(usuario);
    		respuesta = new DtResponse("Contraseña actualizada correctamente",true);  	
        	
        }else {
        	respuesta = new DtResponse("La contraseña actual no coincide ", false);
        }

        return respuesta;
	}

	@Override
	public List<RegistrosPorDia> getRegPorDia() {
		List<RegistrosPorDia> aux_registros = usuarioRepository.getRegPorDia();
		return aux_registros;
	}
	
	
	@Override
	public DtResponse deleteMyUser() throws UsuarioNoExisteException {
		Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
		DtResponse respuesta =  deleteUser(usuario.getId());
		usuarioRepository.delete(usuario);
		
		return respuesta;
	}
	
	@Override
	public ResponseEntity<?> getNotificacionesUsuario() {
		String email = jwtUtil.getEmail();
		Usuario usuarioObj = usuarioRepository.findByEmail(email);
		if(usuarioObj != null) {
			if(usuarioObj instanceof UsuarioFinal) {
				UsuarioFinal usuario = (UsuarioFinal)usuarioObj;
				List<NotificacionUsuario> notificaciones = usuario.getNotificaciones();
				List<DtNotificacionUsuario> dtNotificaciones = new ArrayList<DtNotificacionUsuario>();
				for(NotificacionUsuario notificacion : notificaciones) {
					DtUsuario dtUsuario = new DtUsuarioFinal(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getEmail(), usuario.getFechaNacimiento(), usuario.getFechaAlta(), usuario.getContrasena(), usuario.getImagen(), usuario.getDebitoTotal(), usuario.getCreditoTotal(), usuario.getSaldoTotal(), usuario.isBloqueado());
					
					DtNotificacionUsuario dtNotificacion = new DtNotificacionUsuario(notificacion.getId(), notificacion.getFecha(), notificacion.getTexto(), notificacion.isLeida(), dtUsuario, notificacion.getGrupo().getId());
					dtNotificaciones.add(dtNotificacion);
				}			
				
				return ResponseEntity.ok(dtNotificaciones);
			}else {
				return ResponseEntity.ok(new DtResponse("Administradores no tienen notificaciones", false));
			}
		}else {
			return ResponseEntity.ok(new DtResponse("No se encuentra en el sistema", false));
		}
	}
	
	
	private void updateNombreAvatares(UsuarioFinal usuario, String nuevoNombre) {
		List<Avatar> avatares = usuario.getAvatares();
		
		for(Avatar avatar: avatares) {
			avatar.setNombre(nuevoNombre);
		}
	}
	
	
	public ResponseEntity<?> marcarNotificacionesComoLeidas(){
		UsuarioFinal usuarioObj = (UsuarioFinal)usuarioRepository.findByEmail(jwtUtil.getEmail());
		List<NotificacionUsuario> notificaciones = usuarioObj.getNotificaciones();
		
		for(NotificacionUsuario notificacion: notificaciones) {
			notificacion.setLeida(true);
			
		}
		notificacioRepository.saveAll(notificaciones);
		
		
		return ResponseEntity.ok(new DtResponse("Notificaciones marcadas como leidas correctamente", true));
	}
	
	
	public ResponseEntity<?> marcarNotificacionComoLeida(int notificacionId) throws NoExisteNotificacion{
		
		NotificacionUsuario notificacion = notificacioRepository.findById(notificacionId).orElseThrow(NoExisteNotificacion::new);
		notificacion.setLeida(true);
		notificacioRepository.save(notificacion);
		
		return ResponseEntity.ok(new DtResponse("Notificacion marcada como leida correctamente", true));
	}
	
}




