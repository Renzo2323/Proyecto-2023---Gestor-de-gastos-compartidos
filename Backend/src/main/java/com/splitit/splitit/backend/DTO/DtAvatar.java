package com.splitit.splitit.backend.DTO;

public class DtAvatar {
	
	private int id;
	private String nombre;
	private float saldoLocal;
	private boolean esUsuarioActual;
	private boolean estaVinculado;
	private String imagen;
	
	
	public DtAvatar() {
		
	}
	
	public DtAvatar(int id, String nombre, float saldoLocal, boolean  esUsuarioActual, boolean estaVinculado, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.saldoLocal = saldoLocal;
		this.esUsuarioActual = esUsuarioActual;
		this.estaVinculado = estaVinculado;
		this.imagen = imagen;
	}
	
	public DtAvatar(int id, String nombre, float saldoLocal, boolean  esUsuarioActual, boolean estaVinculado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.saldoLocal = saldoLocal;
		this.esUsuarioActual = esUsuarioActual;
		this.estaVinculado = estaVinculado;
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

    public float getSaldoLocal() {
        return saldoLocal;
    }

    public void setSaldoLocal(float saldoLocal) {
        this.saldoLocal = saldoLocal;
    }

    public boolean isEsUsuarioActual() {
        return esUsuarioActual;
    }

    public void setEsUsuarioActual(boolean esUsuarioActual) {
        this.esUsuarioActual = esUsuarioActual;
    }

    public boolean isEstaVinculado() {
        return estaVinculado;
    }

    public void setEstaVinculado(boolean estaVinculado) {
        this.estaVinculado = estaVinculado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
	
}
