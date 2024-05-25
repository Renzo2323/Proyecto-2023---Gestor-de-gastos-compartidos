package com.splitit.splitit.backend.DTO;

public class DtResponse {
	String mensaje;
    Boolean estatus;
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Boolean getEstatus() {
		return estatus;
	}
	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	public DtResponse(String mensaje, Boolean estatus) {
		super();
		this.mensaje = mensaje;
		this.estatus = estatus;
	}
	public DtResponse() {
		super();
	}
    
    

}
