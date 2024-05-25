package com.splitit.splitit.backend.Exceptions;

public class NoExistePagoException extends Exception {
	public NoExistePagoException() {
		super("El pago no existe");
	}
	
	public NoExistePagoException(String mensaje) {
		super(mensaje);
	}
}
