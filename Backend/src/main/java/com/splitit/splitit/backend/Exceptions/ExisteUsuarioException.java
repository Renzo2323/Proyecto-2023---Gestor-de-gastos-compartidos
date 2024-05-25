package com.splitit.splitit.backend.Exceptions;

public class ExisteUsuarioException extends Exception{
	public ExisteUsuarioException() {
        super("Ya existe un usuario con este email registrado en el sistema");
    }
    public ExisteUsuarioException(String mensaje) {
        super(mensaje);
    }
}
