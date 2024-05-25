package com.splitit.splitit.backend.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.splitit.splitit.backend.DAO.Reset_Token;

public class DtUsuario {
	private int id;
	private String nombre;
	private String apellido;
	private String email;
	private LocalDate fechaNacimiento;
	private LocalDateTime fechaAlta;
	private String contrasena;
	private String imagen;
	@JsonIgnore
	private Reset_Token token;

	
	public DtUsuario() {
		super();
	}
	
	public DtUsuario(int id, String nombre, String apellido, String email, LocalDate fechaNacimiento, LocalDateTime fechaAlta, String contrasena, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaAlta = fechaAlta;
		this.contrasena = contrasena;
		this.imagen = imagen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Reset_Token getToken() {
		return token;
	}

	public void setToken(Reset_Token token) {
		this.token = token;
	}



}
