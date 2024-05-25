package com.splitit.splitit.backend.DTO;

import java.time.LocalDateTime;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;

public class DtAuditoriaUsuario implements Comparable<DtAuditoriaUsuario>{
	
	private String descripcion;
	private LocalDateTime fecha;
	private AuditoriaAccion accion;
	
	
	public DtAuditoriaUsuario() {
		super();
	}
	
	public DtAuditoriaUsuario(String descripcion, LocalDateTime fecha, AuditoriaAccion accion) {
		super();
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.accion = accion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public AuditoriaAccion getAccion() {
		return accion;
	}

	public void setAccion(AuditoriaAccion accion) {
		this.accion = accion;
	}

	@Override
	public int compareTo(DtAuditoriaUsuario o) {
		return fecha.compareTo(o.fecha);
	}
}
