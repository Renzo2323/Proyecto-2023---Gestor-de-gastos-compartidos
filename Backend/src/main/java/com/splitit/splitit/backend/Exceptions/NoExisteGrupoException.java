package com.splitit.splitit.backend.Exceptions;

public class NoExisteGrupoException extends Exception {
	public NoExisteGrupoException() {
		super("El grupo no existe");
	}
	
	public NoExisteGrupoException(String mensaje) {
		super(mensaje);
	}

}
