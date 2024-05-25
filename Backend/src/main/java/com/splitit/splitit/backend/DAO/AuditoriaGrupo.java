package com.splitit.splitit.backend.DAO;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class AuditoriaGrupo {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	@Enumerated(EnumType.STRING)
	private AuditoriaAccion accion;
	@ManyToOne
	private Avatar avatar;
	@Column(nullable = true)
	private int idEntidad;
	private String detalle;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private LocalDateTime createDate;
	

	public AuditoriaGrupo() {
		super();
	}
	
	public AuditoriaGrupo(GrupoDeGastos grupo, AuditoriaAccion accion, Avatar avatar, int idEntidad, String detalle) {
		super();
		this.grupoGastos = grupo;
		this.accion = accion;
		this.avatar = avatar;
		this.idEntidad = idEntidad;
		this.detalle = detalle;
	}
	
	public int getId() {
		return id;
	}
	
	public void setGrupo(GrupoDeGastos grupo) {
		this.grupoGastos = grupo;
	}
	
	public GrupoDeGastos getGrupo() {
		return grupoGastos;
	}
	
	public AuditoriaAccion getAccion() {
		return accion;
	}
	
	public Avatar getAvatar() {
		return avatar;
	}
	
	public int getIdEntidad(){
		return idEntidad;
	}
	
	public String getDetalle() {
		return detalle;
	}

	public LocalDateTime getFecha() {
		return createDate;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + ": " + 
				"\n\tId -> " + this.id + 
				"\n\tAccion -> " + this.accion +
				"\n\tFecha -> " + this.createDate.toString() +
				"\n\tDetalle -> " + this.detalle + 
				"\n\tIdEntidad -> " + this.idEntidad + 
				"\n\tIdAvatar -> " + this.avatar.getId() + 
				"\n\tIdGrupoGastos -> " + this.grupoGastos.getId();
	}
	
}