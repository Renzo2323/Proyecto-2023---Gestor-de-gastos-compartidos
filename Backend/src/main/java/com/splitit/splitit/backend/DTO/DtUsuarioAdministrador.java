package com.splitit.splitit.backend.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtUsuarioAdministrador extends DtUsuario{
	public DtUsuarioAdministrador(int id, String nombre, String apellido, String email, LocalDate fechaNacimiento, LocalDateTime fechaAlta, String contrasena, String imagen) {
		super(id, nombre, apellido, email, fechaNacimiento, fechaAlta, contrasena, imagen);
	}
}
