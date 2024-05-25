package com.splitit.splitit.backend.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtUsuarioFinal extends DtUsuario{
	private float debitoTotal;
	private float creditoTotal;
	private float saldoTotal;
	private boolean bloqueado;
	
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
	public DtUsuarioFinal(int id, String nombre, String apellido, String email, LocalDate fechaNacimiento, LocalDateTime fechaAlta, String contrasena, String imagen, float debitoTotal, float creditoTotal, float saldoTotal, boolean bloqueado) {
		super(id, nombre, apellido, email, fechaNacimiento, fechaAlta, contrasena, imagen);
		this.debitoTotal = debitoTotal;
		this.creditoTotal = creditoTotal;
		this.saldoTotal = saldoTotal;
		this.bloqueado = bloqueado;
	}
	public DtUsuarioFinal() {
		super();
	}
	
	
	
}
