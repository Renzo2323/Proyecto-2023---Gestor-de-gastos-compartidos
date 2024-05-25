package com.splitit.splitit.backend.Services;

import java.time.LocalDateTime;
import java.util.List;

import com.splitit.splitit.backend.DAO.Reset_Token;
import com.splitit.splitit.backend.DTO.DtLogin;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtUsuario;
import com.splitit.splitit.backend.DTO.DtUsuarioFinal;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.Exceptions.ExisteUsuarioException;
import com.splitit.splitit.backend.Exceptions.InvitacionInvalidaException;
import com.splitit.splitit.backend.Exceptions.NoExisteNotificacion;
import com.splitit.splitit.backend.Exceptions.UsuarioBloqueadoException;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;

import org.springframework.http.ResponseEntity;


public interface UsuarioService{
	DtResponse addUsuarioFinal(DtUsuarioFinal dtUsuarioFinal) throws ExisteUsuarioException;
	ResponseEntity<?> loginUsuario(DtLogin dtLogin);
	ResponseEntity<?> loginApp(DtLogin dtLogin);
	List<DtUsuario> getUsuarios();
	DtResponse addUsuarioAdministrador(DtUsuario dtUsuario) throws ExisteUsuarioException;
	String generateResetToken(String email);
	DtResponse resetPassword(String email) throws UsuarioNoExisteException, UsuarioBloqueadoException;
	boolean tokenHasExipred(LocalDateTime vencimiento);
	DtResponse actualizarPassUsuario(DtUsuario dtUsuario);
	DtResponse deleteUser(int usuarioId)throws UsuarioNoExisteException;
	DtResponse editDatosUsuario(int usuarioId, DtUsuario dtUsuario);
	DtResponse darDeBajaUsuario(int usuarioId);
	DtResponse activarUsuario(int usuarioId);
	ResponseEntity<?> getUsuario();
	DtResponse editMisDatosUsuario(DtUsuario dtUsuario);
	DtResponse aceptarInvitacion(String tokenInvitacion) throws InvitacionInvalidaException;
	ResponseEntity<?> getAvatarEmail(int avatarid);
	DtResponse cambiarPass(String oldPass, String newPass);
	List<RegistrosPorDia> getRegPorDia();
	DtResponse deleteMyUser() throws UsuarioNoExisteException;
	ResponseEntity<?> getNotificacionesUsuario();
	ResponseEntity<?> marcarNotificacionesComoLeidas();
	ResponseEntity<?> marcarNotificacionComoLeida(int notificacionId) throws NoExisteNotificacion;
}
