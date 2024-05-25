package com.splitit.splitit.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitit.splitit.backend.DTO.DtCategoria;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Services.CategoriaService;

@RestController
@CrossOrigin
@RequestMapping("api/categorias")
public class CategoriaController {
	@Autowired
	private CategoriaService categoriaService;
	
	@PostMapping(path="/adm/addGlobal")
	public DtResponse addCategoryGlobal(@RequestBody DtCategoria dtCategoria) {
		return categoriaService.addCategoriaGlobal(dtCategoria);
	}
	
	@PostMapping(path="/adm/edit/{categoriaId}")
	public DtResponse editCategoria(@PathVariable int categoriaId, @RequestBody DtCategoria dtCategoria) {
		return categoriaService.editCategoria(categoriaId, dtCategoria);
	}
	
	@PostMapping(path="/usf/edit/{categoriaId}")
	public DtResponse editCategoriaGrupo(@PathVariable int categoriaId, @RequestBody DtCategoria dtCategoria) {
		return categoriaService.editCategoriaGrupo(categoriaId, dtCategoria);
	}
	
	@PostMapping(path="/adm/activar/{categoriaId}")
	public DtResponse activarCategoria(@PathVariable int categoriaId) {
		return categoriaService.activarCategoria(categoriaId, true);
	}
	
	@PostMapping(path="/adm/desactivar/{categoriaId}")
	public DtResponse desactivarCategoria(@PathVariable int categoriaId) {
		return categoriaService.activarCategoria(categoriaId, false);
	}
	
	@PostMapping(path="/usf/activar/{categoriaId}")
	public DtResponse activarCategoriaGrupo(@PathVariable int categoriaId) {
		return categoriaService.activarCategoriaGrupo(categoriaId, true);
	}
	
	@PostMapping(path="/usf/desactivar/{categoriaId}")
	public DtResponse desactivarCategoriaGrupo(@PathVariable int categoriaId) {
		return categoriaService.activarCategoriaGrupo(categoriaId, false);
	}
	
	@PostMapping(path="/adm/delete")
	public DtResponse deleteCategory(@RequestBody int categoriaId) {
		return categoriaService.deleteCategoria(categoriaId);
	}
	
	@GetMapping(path = "/usr")
	public List<DtCategoria> getCategorias(){
		return categoriaService.getCategorias();
	}
	
	@GetMapping(path = "/usf/getByGrupo/{grupoId}")
	public List<DtCategoria> getCategoriasDeGrupo(@PathVariable  int grupoId){
		return categoriaService.getCategoriasDeGrupo(grupoId);
	}
	
	@PostMapping(path="/usf/addGrupo/{grupoId}")
	public DtResponse addCategoryDeGrupo(@PathVariable  int grupoId, @RequestBody DtCategoria dtCategoria) {
		return categoriaService.addCategoriaGrupo(grupoId, dtCategoria);
	}

}


