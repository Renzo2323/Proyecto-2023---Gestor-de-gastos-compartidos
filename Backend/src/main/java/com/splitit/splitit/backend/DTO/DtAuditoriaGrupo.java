package com.splitit.splitit.backend.DTO;

import java.time.LocalDateTime;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;

public class DtAuditoriaGrupo implements Comparable<DtAuditoriaGrupo> {
	
	private String descripcion;
	private LocalDateTime fecha;
	private AuditoriaAccion accion;
	
	
	public DtAuditoriaGrupo() {
		super(); 
	}

	public DtAuditoriaGrupo(String descripcion, LocalDateTime fecha, AuditoriaAccion accion) {
		super();
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.accion = accion;
	}
	
	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	public AuditoriaAccion getAccion() {
		return accion;
	}
	
	public void setAccion(AuditoriaAccion accion) {
		this.accion = accion;
	}

	@Override
	public int compareTo(DtAuditoriaGrupo o) {
		return fecha.compareTo(o.fecha);
	}
}
