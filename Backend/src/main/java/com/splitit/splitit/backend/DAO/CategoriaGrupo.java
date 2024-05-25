package com.splitit.splitit.backend.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CategoriaGrupo extends Categoria{
	
	public CategoriaGrupo(String nombre, String icono, String descripcion, boolean activa) {
		super(nombre, icono, descripcion, activa);
		// TODO Auto-generated constructor stub
	}

	public CategoriaGrupo() {
		
	}
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	public void setGrupoGastos(GrupoDeGastos grupoGastos) {
		this.grupoGastos = grupoGastos;
	}
	
	public GrupoDeGastos getGrupoGastos() {
		return grupoGastos;
	}

}
