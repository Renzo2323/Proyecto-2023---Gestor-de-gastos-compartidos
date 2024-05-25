package com.splitit.splitit.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Services.GastoCompartidoService;

@RestController
@CrossOrigin
@RequestMapping("api/gastosCompartidos")
public class GastoCompartidoController {
	@Autowired
	private GastoCompartidoService gastoCompartidoService;
	
	@PostMapping(path = "/usf/nuevoGastoCompartido")
	public DtResponse ingresarGasto(@RequestBody DtGastoCompartido gastoCompartido) {
		return gastoCompartidoService.nuevoGastoCompartido(gastoCompartido);
	}
	
	@DeleteMapping(path = "/usf/deleteGastoCompartido/{gastoCompartidoId}")
	public DtResponse eliminarGasto(@PathVariable int gastoCompartidoId) {
		return gastoCompartidoService.eliminarGastoCompartido(gastoCompartidoId);
	}

}
