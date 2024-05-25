package com.splitit.splitit.backend.DTO;

public class DtResponseLogin {
	String mensaje;
	String token;
    Boolean estatus;
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean getEstatus() {
		return estatus;
	}
	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	public DtResponseLogin(String mensaje, String token, Boolean estatus) {
		super();
		this.mensaje = mensaje;
		this.token = token;
		this.estatus = estatus;
	}
    
    
}
