package com.splitit.splitit.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.CantGastosPorCategoria;
import com.splitit.splitit.backend.DTO.DtEstadisticasUsuario;
import com.splitit.splitit.backend.DTO.GruposPorDia;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.DTO.TransaccionesPorDia;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.GastoCompartidoService;
import com.splitit.splitit.backend.Services.UsuarioService;


@RestController
@CrossOrigin
@RequestMapping("api/reportes")
public class ReportesController {
	
	@Autowired
    private UsuarioService usuarioService;
	
	@Autowired
	private AuditoriaService auditoriaService;
	
	@Autowired
	private GastoCompartidoService gastoService;
	
	@GetMapping("/adm/RegistrosPorDia")
	public List<RegistrosPorDia> regPorDia() {
		return usuarioService.getRegPorDia();
	}
	
	@GetMapping("/adm/GruposPorDia")
	public List<GruposPorDia> gruposPorDia() {
		return auditoriaService.getGruposPorDia();
	}
	
	@GetMapping("/adm/TransaccionesPorDia")
	public List<TransaccionesPorDia> transaccionesPorDia() {
		return auditoriaService.getTransaccionsPorDia();
	}
	
	@GetMapping("/adm/CantGastosPorCategoria")
	public List<CantGastosPorCategoria> cantGastosCategoria() {
		return gastoService.getCantGastosPorCategoria();
	}
	
	@GetMapping("/usf/getEstadisticasUsuario")
	public List<DtEstadisticasUsuario> getEstadisticasUsuario() {
		return auditoriaService.getEstadisticasUsuario();
	}
	
}
