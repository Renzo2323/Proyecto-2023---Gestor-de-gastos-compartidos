package com.splitit.splitit.backend.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DAO.TipoGrupo;
import com.splitit.splitit.backend.DAO.TipoMoneda;
import com.splitit.splitit.backend.DTO.DtActualizarGrupo;
import com.splitit.splitit.backend.DTO.DtGrupoDeGastos;
import com.splitit.splitit.backend.DTO.DtInvitacion;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Exceptions.AvatarTieneUsuarioException;
import com.splitit.splitit.backend.Exceptions.InvitacionInvalidaException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarEnGrupoException;
import com.splitit.splitit.backend.Exceptions.NoExisteAvatarException;
import com.splitit.splitit.backend.Exceptions.NoExisteGrupoException;
import com.splitit.splitit.backend.Security.JWTUtil;
import com.splitit.splitit.backend.Services.GrupoService;

@RestController
@CrossOrigin
@RequestMapping("api/grupos")
public class GrupoController {
	@Autowired
	private GrupoService grupoService;
	
	@PutMapping(path="/usf/agregarMiGrupoDeGastos")
	public DtResponse addGrupo(@RequestBody DtGrupoDeGastos dtGrupo) {
		DtResponse respuesta;
		try {
			System.out.println("Comienza");
			respuesta = grupoService.addMiGrupo(dtGrupo);
		}catch (Exception e){
			System.out.println("Error");
			return new DtResponse(e.getMessage(),false);
		}
		
		return respuesta;
	}
	
	@DeleteMapping(path = "/usf/eliminarGrupoDeGastos/{grupoId}")
	public DtResponse deleteGrupo(@PathVariable int grupoId) {
		DtResponse respuesta;
		try {
			respuesta = grupoService.deleteGrupo(grupoId);
		}catch (Exception e){

			return new DtResponse(e.getMessage(),false);
		}
		
		return respuesta;
	}
	
	@GetMapping(path = "/adm")
	public List<DtGrupoDeGastos> getGrupos(){
		return grupoService.getGrupos();
	}
	
	@GetMapping(path = "/usf/obtenerMisGrupos")
	public List<DtGrupoDeGastos> getMisGrupos(){
		return grupoService.getMisGrupos();
	}
	
	@GetMapping(path = "/usf/obtenerInfoGrupo/{grupoId}")
	public ResponseEntity<?> obtenerInfoGrupo(@PathVariable int grupoId) {
		try {
			return ResponseEntity.ok(grupoService.obtenerInfoGrupo(grupoId)) ;
		} catch (NoExisteAvatarEnGrupoException e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		}

	}
	
	@PostMapping(path = "/usf/crearAvatarEnGrupo/{grupoId}/{nombre}")
	public ResponseEntity<?> crearAvatarEnGrupo(@PathVariable int grupoId, @PathVariable String nombre) {
		return grupoService.crearAvatarEnGrupo(grupoId, nombre);
	}
	
	@PostMapping(path = "/usf/invitarAGrupo/{grupoId}")
	public DtResponse invitarUsuarioAGrupo(@PathVariable int grupoId, @RequestBody DtInvitacion datosInvitacion) {
		try {
			return grupoService.invitarUsuarioAGrupo(grupoId, datosInvitacion.getAvatarId(), datosInvitacion.getEmail());
		} catch (NoExisteAvatarEnGrupoException | NoExisteGrupoException | NoExisteAvatarException | AvatarTieneUsuarioException e) {
			// TODO Auto-generated catch block
			return new DtResponse(e.getMessage(), false);
		}
	}
	
	@GetMapping(path = "/usr/tipoGrupos")
	public List<TipoGrupo> getTipoGrupos(){
		return grupoService.getTipoGrupos();
	}
	
	@GetMapping(path = "/usr/tipoMonedas")
	public List<TipoMoneda> getTipoMonedas(){
		return grupoService.getTipoMoneda();
	}
	
	@GetMapping(path = "/usf/obtenerInfoGrupoPorToken/{tokenInvitacion}")
	public ResponseEntity<?> obtenerInfoGrupoByInvitacionToken(@PathVariable String tokenInvitacion) {
		try {
			System.out.println("llegue");
			return ResponseEntity.ok(grupoService.obtenerInfoGrupoByInvitacionToken(tokenInvitacion)) ;
		} catch (NoExisteGrupoException e) {
			return ResponseEntity.ok(new DtResponse(e.getMessage(),false));
		}		
	}
	
	@PutMapping(path="/usf/actualizarMiGrupo/{idGrupo}")
	public DtResponse updateGrupo(@RequestBody DtActualizarGrupo dtGrupo, @PathVariable int idGrupo) throws NoExisteGrupoException {
		return grupoService.actualizarGrupo(idGrupo,dtGrupo);
	}
	
	
}
