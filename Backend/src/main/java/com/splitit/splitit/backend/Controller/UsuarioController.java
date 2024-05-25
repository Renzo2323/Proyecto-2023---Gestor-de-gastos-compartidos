package com.splitit.splitit.backend.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Reset_Token;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioFinal;
import com.splitit.splitit.backend.DTO.DtCambioPass;
import com.splitit.splitit.backend.DTO.DtGrupoDeGastos;
import com.splitit.splitit.backend.DTO.DtLogin;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtUsuario;
import com.splitit.splitit.backend.DTO.DtUsuarioAdministrador;
import com.splitit.splitit.backend.DTO.DtUsuarioFinal;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.Exceptions.ExisteUsuarioException;
import com.splitit.splitit.backend.Exceptions.InvitacionInvalidaException;
import com.splitit.splitit.backend.Exceptions.NoExisteNotificacion;
import com.splitit.splitit.backend.Exceptions.UsuarioBloqueadoException;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;
import com.splitit.splitit.backend.Repository.AvatarRepository;
import com.splitit.splitit.backend.Repository.TokenRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.UsuarioService;

import jakarta.mail.MessagingException;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/usuarios")
public class UsuarioController {
	@Autowired
    private UsuarioService usuarioService;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	JWTUtil jwtutil;
	
	//------------------ FUNCIONES POST ------------------//
	
	@PostMapping(path = "/all/agregarUsuarioFinal")
    public DtResponse addUsuarioFinal(@RequestBody DtUsuarioFinal dtUsuarioFinal){
		DtResponse respuesta;
		try {
			respuesta = usuarioService.addUsuarioFinal(dtUsuarioFinal);
		}catch (ExisteUsuarioException e){
			return new DtResponse(e.getMessage(),false);
		}
        
		return respuesta;
    }

    @PostMapping(path = "/usr/actualizarMisDatosUsuario")
    public DtResponse editMisDatosUsuario(@RequestBody DtUsuario dtUsuario){
		DtResponse respuesta;
		try {
			respuesta = usuarioService.editMisDatosUsuario(dtUsuario);
		}catch (Exception e){
			return new DtResponse(e.getMessage(),false);
		}
        
		return respuesta;
    }
	
	@PostMapping(path = "/adm/agregarAdministrador")
    public DtResponse addUsuarioAdministrador(@RequestBody DtUsuario dtUsuario){
		DtResponse respuesta;
		try {
			respuesta = usuarioService.addUsuarioAdministrador(dtUsuario);
		}catch (ExisteUsuarioException e){
			return new DtResponse(e.getMessage(),false);
		}
		
		return respuesta;
    }
    
	@PostMapping(path = "/all/login")
    public ResponseEntity<?> loginEmployee(@RequestBody DtLogin loginDTO)
    {
        return usuarioService.loginUsuario(loginDTO);
    }
	
	@PostMapping(path = "/all/loginApp")
    public ResponseEntity<?> loginApp(@RequestBody DtLogin loginDTO)
    {
        return usuarioService.loginApp(loginDTO);
    }
	
	@PostMapping(path = "/all/requestPassword")
    public DtResponse resetPassword(@RequestBody String email){
		DtResponse respuesta;
		System.out.println("El email es: " +email);
		try {
			respuesta = usuarioService.resetPassword(email);
		}catch(UsuarioNoExisteException | UsuarioBloqueadoException e) {
			return new DtResponse(e.getMessage(),false);
		}
        return respuesta;
    }
	
	@PostMapping("/all/resetPassword")
	public String passwordResetProcess(@RequestBody DtUsuario dtUsuario) {
		System.out.println("El usuario es: "+dtUsuario.getEmail());
		usuarioService.actualizarPassUsuario(dtUsuario);
		return "redirect:/iniciarSesion";
	}
	
	@PostMapping(path = "/adm/activarUsuario/{usuarioId}")
    public DtResponse activarUsuario(@PathVariable int usuarioId)
    {
        return usuarioService.activarUsuario(usuarioId);
    }
	
	@PostMapping(path = "/adm/desactivarUsuario/{usuarioId}")
    public DtResponse darDeBajaUsuario(@PathVariable int usuarioId)
    {
		return usuarioService.darDeBajaUsuario(usuarioId);
    }
	
	@PostMapping(path = "/usf/aceptarInvitacion")
	public DtResponse aceptarInvitacion(@RequestBody String tokenInvitacion) {
		try {
			return usuarioService.aceptarInvitacion(tokenInvitacion);
		} catch (InvitacionInvalidaException e) {
			return new DtResponse(e.getMessage(), false);
		}
	}
	
	@PostMapping(path = "/usr/cambiarPass")
	public DtResponse cambiarPass(@RequestBody DtCambioPass dtContraseñas){
		return usuarioService.cambiarPass(dtContraseñas.getOldPass(), dtContraseñas.getNewPass());
	}
	
	@PostMapping(path = "/usf/marcarNotificacionesComoLeidas")
	public ResponseEntity<?> marcarNotificacionesComoLeidas() {
		return usuarioService.marcarNotificacionesComoLeidas();
	}
	
	@PostMapping(path = "/usf/marcarNotificacionComoLeida/{notificacionId}")
	public ResponseEntity<?> marcarNotificacionComoLeida(@PathVariable int notificacionId) {
		try {
			return usuarioService.marcarNotificacionComoLeida(notificacionId);
		}
		catch (NoExisteNotificacion e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		}
	}
	
	//------------------ FUNCIONES GET ------------------//
	
	@GetMapping(path = "/adm/")
	public List<DtUsuario> getUsuarios(){
		return usuarioService.getUsuarios();
	}
	
	@GetMapping(path = "/usr/infoUsuario") //Reorna informacion del usuario por token
	public ResponseEntity<?> getUsuario(){
		return usuarioService.getUsuario();
	}
	
	@GetMapping(path = "/usr/notificacionesUsuario") 
	public ResponseEntity<?> getNotificacionesUsuario(){
		return usuarioService.getNotificacionesUsuario();
	}
	
	@GetMapping("/all/resetPassword/{token}")
	public String resetPasswordForm(@PathVariable String token, Model model) {
		Reset_Token aux_token = tokenRepository.findByToken(token);
		if (aux_token != null && usuarioService.tokenHasExipred(aux_token.getVencimiento())) {
			model.addAttribute("email", aux_token.getUsuario().getEmail());
			//continua al endpoint "resetPassword"
			return aux_token.getUsuario().getEmail();
		}
		//La idea aca es redirigir a una url que decida el frontend para mensaje de error al cambiar contraseña
		return "error";
	}
	
	@GetMapping("/all/rol")
	public ResponseEntity<?>  getRol() {
		return ResponseEntity.ok(new DtResponse(jwtutil.getRol(),false));
	}
	
	@GetMapping("/usf/getAvatarEmail/{avatarId}")
	public ResponseEntity<?> getAvatarEmail(@PathVariable int avatarId) {
		return usuarioService.getAvatarEmail(avatarId);
	}
	
	
	//------------------ FUNCIONES DELETE ------------------//
	
	@DeleteMapping("/adm/borrarUsuario/{user_id}")
    public DtResponse deleteUser(@PathVariable("user_id") int usuarioId) {
		DtResponse respuesta;
        try {
            respuesta = usuarioService.deleteUser(usuarioId);
        } catch (UsuarioNoExisteException e) {
        	return new DtResponse(e.getMessage(),false);
        }
        return respuesta;
    }
	
	@DeleteMapping("/usr/borrarMiUsuario")
    public DtResponse deleteMyUser() {
		DtResponse respuesta;
        try {
            respuesta = usuarioService.deleteMyUser();
        } catch (UsuarioNoExisteException e) {
        	return new DtResponse(e.getMessage(),false);
        }
        return respuesta;
    }
		

}
