package com.splitit.splitit.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.DtAuditoriaGrupo;
import com.splitit.splitit.backend.DTO.DtAuditoriaUsuario;
import com.splitit.splitit.backend.Exceptions.UsuarioNoExisteException;
import com.splitit.splitit.backend.DTO.GruposPorDia;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.Services.AuditoriaService;

@RestController
@CrossOrigin
@RequestMapping("api/auditoria")
public class AuditoriaController {
	@Autowired
	private AuditoriaService auditoriaService;
	
	
	@GetMapping(path = "/usr/grupo/{grupoId}")
	public List<DtAuditoriaGrupo> getAuditoriaGrupo(@PathVariable int grupoId){
		return auditoriaService.getAuditoriaGrupo(grupoId);		
	}
	
	@GetMapping("/adm/resumenActividadUsuario/{usuarioId}")
	public List<DtAuditoriaUsuario> getAuditoriaUsuario(@PathVariable int usuarioId) {
		try {
			return auditoriaService.getAuditoriaUsuario(usuarioId);
		} catch (UsuarioNoExisteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
