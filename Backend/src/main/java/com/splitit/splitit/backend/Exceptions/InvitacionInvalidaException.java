package com.splitit.splitit.backend.Exceptions;

public class InvitacionInvalidaException extends Exception {
	public InvitacionInvalidaException() {
		super("Token invalido");
	}
	
	public InvitacionInvalidaException(String mensaje) {
		super(mensaje);
	}
}
