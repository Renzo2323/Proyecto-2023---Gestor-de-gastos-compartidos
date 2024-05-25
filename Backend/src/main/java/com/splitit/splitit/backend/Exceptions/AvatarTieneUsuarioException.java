package com.splitit.splitit.backend.Exceptions;

public class AvatarTieneUsuarioException extends Exception {
	public AvatarTieneUsuarioException() {
		super("Este avatar ya tiene un usuario asociado");
	}
	
	public AvatarTieneUsuarioException(String mensaje) {
		super(mensaje);
	}
}
