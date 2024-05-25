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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Pago {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private float monto;
	@Enumerated(EnumType.STRING)
	private TipoMoneda moneda;
	
	@Enumerated(EnumType.STRING)
	private MetodoPago metodoPago;
	private String nota;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	@ManyToOne
	private Avatar pagador;
	
	@ManyToOne
	private Avatar receptor;
	
	public Pago() {};
	
	public Pago(float monto, TipoMoneda moneda, MetodoPago metodoPago, String nota, GrupoDeGastos grupoGastos, Avatar pagador, Avatar receptor){
		super();
		this.monto = monto;
		this.moneda = moneda;
		this.metodoPago = metodoPago;
		this.nota = nota;
		this.grupoGastos = grupoGastos;
		this.pagador = pagador;
		this.receptor = receptor;
	}
	
	public void setMonto(float monto) {
		this.monto = monto;
	}
	
	public void setMoneda(TipoMoneda moneda) {
		this.moneda = moneda;
	}
	
	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public void setNota(String nota) {
		this.nota = nota;
	}
	
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	public void setGrupoGasto(GrupoDeGastos grupo) {
		this.grupoGastos = grupo;
	}
	
	public void setPagador(Avatar pagador) {
		this.pagador = pagador;
	}
	
	public void setRecividor(Avatar receptor) {
		this.receptor = receptor;
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
	
	public GrupoDeGastos getGrupo() {
		return grupoGastos;		
	}
	
	public Avatar getPagador() {
		return pagador;
	}
	
	public Avatar getReceptor() {
		return receptor;
	}
	
	

}
