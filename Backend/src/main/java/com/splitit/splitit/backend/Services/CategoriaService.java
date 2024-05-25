package com.splitit.splitit.backend.Services;

import java.util.List;

import com.splitit.splitit.backend.DTO.DtCategoria;
import com.splitit.splitit.backend.DTO.DtResponse;

public interface CategoriaService {
	DtResponse addCategoriaGlobal(DtCategoria dtCategoria);
	DtResponse editCategoria(int categoriaId, DtCategoria dtCategoria);
	DtResponse deleteCategoria(int categoriaId);
	List<DtCategoria> getCategorias();
	List<DtCategoria> getCategoriasDeGrupo(int grupoId);
	DtResponse addCategoriaGrupo(int grupoId, DtCategoria dtCategoria);
	DtResponse activarCategoria(int categoriaId, boolean activa);
	DtResponse editCategoriaGrupo(int categoriaId, DtCategoria dtCategoria);
	DtResponse activarCategoriaGrupo(int categoriaId, boolean activa);
	
}
