package com.splitit.splitit.backend.DAO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class NotificacionUsuario {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private LocalDateTime fecha;
	private String texto;
	private boolean leida;
	
	@ManyToOne
	@JsonIgnore
	private UsuarioFinal usuario;
	
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	public NotificacionUsuario() {
		super();
	}
	
	public NotificacionUsuario(LocalDateTime fecha, String texto, boolean leida, UsuarioFinal usuario, GrupoDeGastos grupo) {
		super();
		this.fecha = fecha;
		this.texto = texto;
		this.leida = leida;
		this.usuario = usuario;
		this.grupoGastos = grupo;
	}
	
	public NotificacionUsuario(int id, LocalDateTime fecha, String texto, boolean leida, UsuarioFinal usuario, GrupoDeGastos grupo) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.texto = texto;
		this.leida = leida;
		this.usuario = usuario;
		this.grupoGastos = grupo;
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getTexto() {
		return texto;
	}

	public boolean isLeida() {
		return leida;
	}
	
	public UsuarioFinal getUsuario() {
		return usuario;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}

	public void setUsuario(UsuarioFinal usuario) {
		this.usuario = usuario;
	}
	
	public void setGrupo(GrupoDeGastos grupo) {
		this.grupoGastos = grupo;
	}
	
	public GrupoDeGastos getGrupo() {
		return grupoGastos;
	}
}
