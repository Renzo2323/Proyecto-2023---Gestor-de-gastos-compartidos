package com.splitit.splitit.backend.Exceptions;

public class TokenVencidoException extends Exception{
	public TokenVencidoException() {
        super("El token de reseteo expiro");
    }
    public TokenVencidoException(String mensaje) {
        super(mensaje);
    }
}
