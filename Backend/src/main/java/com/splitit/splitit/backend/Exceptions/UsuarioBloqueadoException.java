package com.splitit.splitit.backend.Exceptions;

public class UsuarioBloqueadoException extends Exception{
	public UsuarioBloqueadoException() {
        super("El usuario esta bloqueado");
    }
    public UsuarioBloqueadoException(String mensaje) {
        super(mensaje);
    }
}
