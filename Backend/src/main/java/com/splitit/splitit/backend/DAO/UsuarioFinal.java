package com.splitit.splitit.backend.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class UsuarioFinal extends Usuario{
	private float debitoTotal;
	private float creditoTotal;
	private float saldoTotal;
	private boolean bloqueado;
	
	@OneToMany(mappedBy="usuario")
	private List<Avatar> avatares;

	@OneToMany(mappedBy="usuario")
	private List<NotificacionUsuario> notificaciones;

	public UsuarioFinal(String nombre, String apellido,  String email, LocalDate fechaNacimiento, LocalDateTime fechaAlta, String contrasena, String imagen,
			Reset_Token token, float debitoTotal, float creditoTotal, float saldoTotal, boolean bloqueado,
			List<Avatar> avatares) {
		super(nombre, apellido, email, fechaNacimiento, fechaAlta, contrasena, imagen, token);
		this.debitoTotal = debitoTotal;
		this.creditoTotal = creditoTotal;
		this.saldoTotal = saldoTotal;
		this.bloqueado = bloqueado;
		this.avatares = avatares;
	}

	public float getDebitoTotal() {
		return debitoTotal;
	}

	public void setDebitoTotal(float debitoTotal) {
		this.debitoTotal = debitoTotal;
	}

	public float getCreditoTotal() {
		return creditoTotal;
	}

	public void setCreditoTotal(float creditoTotal) {
		this.creditoTotal = creditoTotal;
	}

	public float getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(float saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public List<Avatar> getAvatares() {
		return avatares;
	}

	public void setAvatares(List<Avatar> avatares) {
		this.avatares = avatares;
	}
	
	public void addAvatar(Avatar avatar) {
		this.avatares.add(avatar);
	}

	public UsuarioFinal(int id, String nombre, String apellido, LocalDate fechaNacimiento, String email, LocalDateTime fechaAlta, String contrasena, String imagen,
			Reset_Token token) {
		super(nombre, apellido, email, fechaNacimiento, fechaAlta, contrasena, imagen, token);
	}

	public UsuarioFinal() {
	}

	public List<NotificacionUsuario> getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(List<NotificacionUsuario> notificaciones) {
		this.notificaciones = notificaciones;
	}
	



}
