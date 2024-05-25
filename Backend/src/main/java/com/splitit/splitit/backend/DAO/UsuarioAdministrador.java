package com.splitit.splitit.backend.DAO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Entity
public class UsuarioAdministrador extends Usuario{
	
	public UsuarioAdministrador(String nombre, String apellido, String email, LocalDate fechaNacimiento, LocalDateTime fechaAlta, String contrasena, String imagen,
			Reset_Token token) {
		super(nombre, apellido, email, fechaNacimiento, fechaAlta, contrasena, imagen, token);
	}
	
	public UsuarioAdministrador() {
	}
}
