package com.splitit.splitit.backend.Services;

import java.util.List;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DTO.DtAuditoriaGrupo;
import com.splitit.splitit.backend.DTO.DtAuditoriaUsuario;
import com.splitit.splitit.backend.DTO.DtEstadisticasUsuario;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;
import com.splitit.splitit.backend.DTO.GruposPorDia;
import com.splitit.splitit.backend.DTO.TransaccionesPorDia;

public interface AuditoriaService {
	void addAuditoriaGrupo(int grupoId, AuditoriaAccion accion, Avatar avatar1, int idEntidad, String detalle);
	List<DtAuditoriaGrupo> getAuditoriaGrupo(int grupoId);
	List<DtAuditoriaUsuario> getAuditoriaUsuario(int usuarioId) throws UsuarioNoExisteException;
	List<GruposPorDia> getGruposPorDia();
	List<TransaccionesPorDia> getTransaccionsPorDia();
	List<DtEstadisticasUsuario> getEstadisticasUsuario();
}
