package com.splitit.splitit.backend.Exceptions;

public class NoExisteAvatarException extends Exception {
	public NoExisteAvatarException() {
		super("El avatar no existe");
	}
	
	public NoExisteAvatarException(String mensaje) {
		super(mensaje);
	}

}
