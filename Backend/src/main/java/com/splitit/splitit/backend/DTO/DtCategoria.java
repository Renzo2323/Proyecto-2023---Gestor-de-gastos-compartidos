package com.splitit.splitit.backend.DTO;

import java.time.LocalDateTime;

public class DtCategoria {
	private int id;
	private String nombre;
	private String icono;
	private String descripcion;
	private boolean activa;
	
	public DtCategoria(int id, String nombre, String icono, String descripcion, boolean activa) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.icono = icono;
		this.descripcion = descripcion;
		this.activa = activa;
	}
	
	public DtCategoria(int id) {
		super();
		this.id = id;
	}
	
	public DtCategoria() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIcono() {
		return icono;
	}
	
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	public String getDescripcion(){
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public boolean getEstado() {
		return activa;
	}
	
	public void setEstado(boolean activa) {
		this.activa = activa;
	}
}
	
	
