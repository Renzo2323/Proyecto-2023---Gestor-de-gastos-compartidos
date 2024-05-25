package com.splitit.splitit.backend.DTO;

public class DtParametro {
	
	private int id;
	private String nombre;
	private String valor;
	
	public DtParametro() {
		super();
	}
	
	public DtParametro(String nombre, String valor) {
		super();
		this.nombre = nombre;
		this.valor = valor;
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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
