package com.splitit.splitit.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.DtParametro;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Services.ParametroService;

@RestController
@CrossOrigin
@RequestMapping("api/parametros")
public class ParametroController {
	@Autowired
	private ParametroService parametroService;
	
	@GetMapping(path = "/adm/")
	public List<DtParametro> getParametros() {
		return parametroService.getParametros();
	}
	
	@PostMapping(path = "/adm/agregarParametro")
	public DtResponse crearParametro(@RequestBody DtParametro dtParametro) {
		return parametroService.crearParametro(dtParametro);
	}
	
	@PostMapping(path = "/adm/modificarParametro/")
	public DtResponse modificarParametro(@RequestBody DtParametro dtParametro) {
		return parametroService.modificarParametro(dtParametro);
	}
	
	@DeleteMapping(path = "/adm/eliminarParametro/{parametroId}")
	public DtResponse eliminarParametro(@PathVariable int parametroId) {
		return parametroService.eliminarParametro(parametroId);
	}
	
	@PostMapping(path = "/adm/reemplazarParametros/")
	public DtResponse reemplazarParametros(@RequestBody List<DtParametro> dtParametros) {
		return parametroService.reemplazarParametros(dtParametros);
	}

}
