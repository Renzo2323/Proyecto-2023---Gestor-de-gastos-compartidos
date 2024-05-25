package com.splitit.splitit.backend.DAO;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Avatar {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private String nombre;
	private float saldoLocal;
	private String tokenInvitacion;
	
	@OneToMany(mappedBy="deudor")
	private List<Deuda> debitos;
	
	@OneToMany(mappedBy="acreedor")
	private List<Deuda> creditos;
	
	@OneToMany(mappedBy="pagador")
	private List<Pago> pagosHechos;
	
	@OneToMany(mappedBy="receptor")
	private List<Pago> pagosRecibidos;
	
	@OneToMany(mappedBy="avatar")
	private List<AuditoriaGrupo> auditoriaGrupos;
	
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	@ManyToOne
	private UsuarioFinal usuario;
	
	public Avatar() {
		super();
	}
	
	public Avatar(String nombre, float saldoLocal, GrupoDeGastos grupoGastos, UsuarioFinal usuario) {
		super();
		this.nombre = nombre;
		this.saldoLocal = saldoLocal;
		this.grupoGastos = grupoGastos;
		this.usuario = usuario;		
	}
	
	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public float getSaldoLocal() {
		return saldoLocal;
	}
	
	public void setSaldoLocal(float saldoLocal) {
		this.saldoLocal = saldoLocal;
	}
	
	public GrupoDeGastos getGrupoGastos() {
		return grupoGastos;
	}
	
	public void setGrupoGastos(GrupoDeGastos grupoGastos) {
		this.grupoGastos = grupoGastos;
	}
	
	public int getIdGrupoDeGastos() {
		return grupoGastos.getId();
	}
	
	public UsuarioFinal getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(UsuarioFinal usuario) {
		this.usuario = usuario;
	}

	public String getTokenInvitacion() {
		return tokenInvitacion;
	}

	public void setTokenInvitacion(String tokenInvitacion) {
		this.tokenInvitacion = tokenInvitacion;
	}
	
	
}
