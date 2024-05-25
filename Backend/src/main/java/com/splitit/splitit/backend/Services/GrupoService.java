package com.splitit.splitit.backend.Services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.splitit.splitit.backend.DAO.TipoGrupo;
import com.splitit.splitit.backend.DAO.TipoMoneda;
import com.splitit.splitit.backend.DTO.DtActualizarGrupo;
import com.splitit.splitit.backend.DTO.DtGrupoDeGastos;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Exceptions.AvatarTieneUsuarioException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarEnGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarException;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;

public interface GrupoService {
	DtResponse addGrupo(int usuarioId, DtGrupoDeGastos dtGrupo);
	DtResponse deleteGrupo(int grupoId);
	List<DtGrupoDeGastos> getGrupos();
	List<DtGrupoDeGastos> getGrupoByUsuario(int usuarioId);
	boolean grupoTieneSaldos(int grupoId);
	ResponseEntity<?> crearAvatarEnGrupo(int grupoId, String nombre);
	//DtResponse aceptarInvitacion(int usuarioId, int grupoId);
	DtResponse invitarUsuarioAGrupo(int grupoId, int avatarId, String email) throws NoExisteAvatarEnGrupoException, NoExisteGrupoException, NoExisteAvatarException, AvatarTieneUsuarioException;
	List<TipoGrupo> getTipoGrupos();
	List<TipoMoneda>getTipoMoneda();
	List<DtGrupoDeGastos> getMisGrupos();
	DtResponse addMiGrupo(DtGrupoDeGastos dtGrupo);
	DtGrupoDeGastos obtenerInfoGrupo(int grupoId) throws NoExisteAvatarEnGrupoException;
	DtGrupoDeGastos obtenerInfoGrupoByInvitacionToken(String token) throws NoExisteGrupoException;
	DtResponse actualizarGrupo(int idGrupo, DtActualizarGrupo dtGrupo) throws NoExisteGrupoException;
}
