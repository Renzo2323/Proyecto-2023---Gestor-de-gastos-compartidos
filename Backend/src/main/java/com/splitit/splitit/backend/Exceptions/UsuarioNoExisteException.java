package com.splitit.splitit.backend.Exceptions;

public class UsuarioNoExisteException extends Exception{
	public UsuarioNoExisteException() {
        super("El usuario ingresado no existe");
    }
    public UsuarioNoExisteException(String mensaje) {
        super(mensaje);
    }
}
