package com.splitit.splitit.backend.DAO;

import jakarta.persistence.Entity;

@Entity
public class CategoriaGlobal extends Categoria {

	public CategoriaGlobal(String nombre, String icono, String descripcion, boolean activa) {
		super(nombre, icono, descripcion, activa);
		// TODO Auto-generated constructor stub
	}
	
	public CategoriaGlobal() {
		
	}

}
