package com.splitit.splitit.backend.DAO;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Reset_Token {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private String token;
	private LocalDateTime vencimiento;
	
	@OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(LocalDateTime vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Reset_Token(int id, String token, LocalDateTime vencimiento, Usuario usuario) {
		super();
		this.id = id;
		this.token = token;
		this.vencimiento = vencimiento;
		this.usuario = usuario;
	}

	public Reset_Token() {
		super();
	}

}
