package com.splitit.splitit.backend.DTO;

import java.time.LocalDateTime;

import com.splitit.splitit.backend.DAO.MetodoPago;
import com.splitit.splitit.backend.DAO.TipoMoneda;

public class DtPago {

	private int id;
	private float monto;
	private TipoMoneda moneda;
	private MetodoPago metodoPago;
	private String nota;
	
	private LocalDateTime fecha;
	
	private int grupoId;
	
	private int avatarPagador; 
	
	private int avatarReceptor;
	
	public DtPago(){};
	
	public DtPago(int id, float monto, TipoMoneda moneda, MetodoPago metodoPago, String nota) {
		this.id = id;
		this.monto = monto;
		this.moneda = moneda;
		this.metodoPago = metodoPago;
		this.nota = nota;
	}
	
	public DtPago(int id, float monto, TipoMoneda moneda, MetodoPago metodoPago, String nota, LocalDateTime fecha) {
		this.id = id;
		this.monto = monto;
		this.moneda = moneda;
		this.metodoPago = metodoPago;
		this.nota = nota;
		this.fecha = fecha;
	}
	
	public DtPago(int id, float monto, TipoMoneda moneda, MetodoPago metodoPago, String nota, 
            int grupoId, int avatarPagador, int avatarReceptor) {
		 this.id = id;
		 this.monto = monto;
		 this.moneda = moneda;
		 this.metodoPago = metodoPago;
		 this.nota = nota;
		 this.grupoId = grupoId;
		 this.avatarPagador = avatarPagador;
		 this.avatarReceptor = avatarReceptor;
		}
	
	public DtPago(int id, float monto, TipoMoneda moneda, MetodoPago metodoPago, String nota, LocalDateTime fecha, 
            int grupoId, int avatarPagador, int avatarReceptor) {
		 this.id = id;
		 this.monto = monto;
		 this.moneda = moneda;
		 this.metodoPago = metodoPago;
		 this.nota = nota;
		 this.fecha = fecha;
		 this.grupoId = grupoId;
		 this.avatarPagador = avatarPagador;
		 this.avatarReceptor = avatarReceptor;
		}
	
	public int getId() {
		return id;
	}
	
	public float getMonto() {
		return monto;
	}
	
	public TipoMoneda getMoneda() {
		return moneda;
	}
	
	public MetodoPago getMetodoPago() {
		return metodoPago;
	}
	
	public String getNota() {
		return nota;
	}
	
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	public int getGrupoId() {
		return grupoId;
	}
	
	public int getAvatarPagador() {
		return avatarPagador;
	}
	
	public int getAvatarReceptor() {
		return avatarReceptor;
	}
	
	public void setAvatarPagador(int avatarPagador) {
		this.avatarPagador = avatarPagador;
	}
	
	public void setAvatarReceptor(int avatarReceptor) {
		this.avatarReceptor = avatarReceptor;
	}
	
	public void SetGrupoId(int grupoId) {
		this.grupoId = grupoId;
	}
	
}
