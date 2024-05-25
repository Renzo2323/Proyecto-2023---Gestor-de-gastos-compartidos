package com.splitit.splitit.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.DtAvatar;
import com.splitit.splitit.backend.DTO.DtPago;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtSaldo;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarEnGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExistePagoException;
import com.splitit.splitit.backend.Services.PagoService;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/pagos")
public class PagoController {
	
	@Autowired
	private PagoService pagoService;
	
	
	@PostMapping(path = "/usf/nuevoPago")
	public DtResponse ingresarPago(@RequestBody DtPago pago) {
		return pagoService.nuevoPago(pago);
	}
	
	@GetMapping(path = "/usf/getSaldosDeGrupo/{grupoId}")
	public List<DtSaldo> getSaldosDeGrupo(@PathVariable int grupoId){
		return pagoService.getSaldosByGrupo(grupoId);
	}
	
	@GetMapping(path = "/usf/getSaldosSimplificadosDeGrupo/{grupoId}")
	public List<DtSaldo> getSaldosSimplificadosDeGrupo(@PathVariable int grupoId){
		return pagoService.getSaldosSimplificadosByGrupo(grupoId);
	}
	
	@GetMapping(path = "/usf/getSaldosSimplificadosDeGrupoNew/{grupoId}")
	public ResponseEntity<?> getSaldosSimplificadosDeGrupoNew(@PathVariable int grupoId){	
		try {
			return ResponseEntity.ok(pagoService.getSaldosSimplificadosByGrupoNew(grupoId));
		} catch (NoExisteGrupoException e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		} catch (Exception e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		}
	}
	
	
	@DeleteMapping(path = "/usf/deletePago/{pagoId}")
	public ResponseEntity<?> eliminarGasto(@PathVariable int pagoId) {
		try {
			return ResponseEntity.ok(pagoService.eliminarPago(pagoId));
		} catch (NoExistePagoException e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		}
	}

}
