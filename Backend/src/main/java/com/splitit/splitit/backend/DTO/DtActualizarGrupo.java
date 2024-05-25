package com.splitit.splitit.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import com.splitit.splitit.backend.DAO.TipoGrupo;
import com.splitit.splitit.backend.DAO.TipoMoneda;

public class DtActualizarGrupo {
	private String nombre;
	private TipoGrupo tipoGrupo;
	private TipoMoneda moneda;
	private String imagen;
	private List<DtCategoriaGrupo> categoriasGrupo = new ArrayList<DtCategoriaGrupo>();
	

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public TipoGrupo getTipoGrupo() {
		return tipoGrupo;
	}
	public void setTipoGrupo(TipoGrupo tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public List<DtCategoriaGrupo> getCategoriasGrupo() {
		return categoriasGrupo;
	}
	public void setCategoriasGrupo(List<DtCategoriaGrupo> categoriasGrupo) {
		this.categoriasGrupo = categoriasGrupo;
	}
	public DtActualizarGrupo( String nombre, TipoGrupo tipoGrupo, String imagen,
			List<DtCategoriaGrupo> categoriasGrupo) {
		super();
		this.nombre = nombre;
		this.tipoGrupo = tipoGrupo;
		this.imagen = imagen;
		this.categoriasGrupo = categoriasGrupo;
	}
	public DtActualizarGrupo() {
		super();
	}
	public TipoMoneda getMoneda() {
		return moneda;
	}
	public void setMoneda(TipoMoneda moneda) {
		this.moneda = moneda;
	};
	
	
}
