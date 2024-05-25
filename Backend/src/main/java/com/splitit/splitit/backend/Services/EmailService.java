package com.splitit.splitit.backend.Services;

import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Exceptions.MailingException;

public interface EmailService {
    void sendResetToken(Usuario usuario, String tokenURL) throws MailingException;

	void sendInvitacionToken(String mail, String invitacionURL) throws MailingException;
	
	void sendNotificacionGasto(String mail, String textoNotificacion) throws MailingException;
	
	void sendNotificacionPagoEnviado(String mail, String textoNotificacion) throws MailingException;
	
	void sendNotificacionPagoRecibido(String mail, String textoNotificacion) throws MailingException;
}
