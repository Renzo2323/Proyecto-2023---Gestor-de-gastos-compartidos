package com.splitit.splitit.backend.Exceptions;

public class NoExisteNotificacion extends Exception{
	public NoExisteNotificacion() {
		super("La notificacion no existe");
	}
	
	public NoExisteNotificacion(String mensaje) {
		super(mensaje);
	}

}
