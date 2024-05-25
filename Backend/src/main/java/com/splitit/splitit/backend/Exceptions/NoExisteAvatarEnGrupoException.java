package com.splitit.splitit.backend.Exceptions;

public class NoExisteAvatarEnGrupoException extends Exception {
	
	public NoExisteAvatarEnGrupoException() {
        super("El usuario no pertenece al grupo.");
    }
	
	public NoExisteAvatarEnGrupoException(String mensaje) {
        super(mensaje);
    }

}
