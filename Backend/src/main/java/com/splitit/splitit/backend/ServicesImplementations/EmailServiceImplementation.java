package com.splitit.splitit.backend.ServicesImplementations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.Usuario;

import com.splitit.splitit.backend.Exceptions.MailingException;
import com.splitit.splitit.backend.Services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImplementation implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendResetToken(Usuario usuario, String tokenURL) throws MailingException {
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");
		String htmlMsg = "<h3>¡Hola!</h3>"
				+ "<p>Parece que te olvidaste de tu contraseña, no te peocupes, haciendo click en el siguiente link podras introducir una nueva contraseña:"
				+ "<a href=\"" + tokenURL + "\"> Reestablecer Contraseña</a></p>"
				+ "<p>En caso de que quieras reestrablecer tu contraseña usando la aplicacion, tu codigo es: "
				+ "<b>"	+ tokenURL.substring(tokenURL.length()-9) + "</b></p>"
				+ "<br>"
				+ "<p>¡Saludos desde el equipo de Split it! 👋</p>";
		
		try {
			helper.setFrom("split.itnoreplay@gmail.com");
			helper.setTo(usuario.getEmail());
			helper.setSubject("Split it - Reestablecer contraseña");
			helper.setText(htmlMsg, true);
			
			javaMailSender.send(mimeMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		javaMailSender.send(mimeMsg);
	}
	
	@Override
	public void sendInvitacionToken(String mail, String invitacionURL) throws MailingException {		
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");
		String htmlMsg = "<h3>¡Hola!</h3>"
				+ "<p>Un amigo te ha invitado a unirte a un grupo de gastos, has click en el siguiente link para aceptar la invitacion: "
				+ "<a href=\"" + invitacionURL + "\"> Aceptar Invitacion</a></p>"
				+ "<p>En caso de que quieras unirte al grupo usando la aplicacion, tu codigo es el siguiente: "
				+ "<b>"	+ invitacionURL.substring(invitacionURL.length()-9) + "</b></p>"
				+ "<br>"
				+ "<p>¡Saludos desde el equipo de Split it! 👋</p>";
		try {
			helper.setFrom("split.itnoreplay@gmail.com");
			helper.setTo(mail);
			helper.setSubject("Split it - Invitacion a nuevo grupo de gastos");
			helper.setText(htmlMsg, true);
			
			javaMailSender.send(mimeMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendNotificacionGasto(String mail, String textoNotificacion) throws MailingException {
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");
		String htmlMsg = "<h3>¡Hola!</h3>"
				+ "<p>" + textoNotificacion + "</p>"
				+ "<br>"
				+ "<p>¡Saludos desde el equipo de Split it! 👋</p>";
		try {
			helper.setFrom("split.itnoreplay@gmail.com");
			helper.setTo(mail);
			helper.setSubject("Split it - Te han agregado a un gasto");
			helper.setText(htmlMsg, true);
			
			javaMailSender.send(mimeMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendNotificacionPagoEnviado(String mail, String textoNotificacion) throws MailingException {
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");
		String htmlMsg = "<h3>¡Hola!</h3>"
				+ "<p>" + textoNotificacion + "</p>"
				+ "<br>"
				+ "<p>¡Saludos desde el equipo de Split it! 👋</p>";
		try {
			helper.setFrom("split.itnoreplay@gmail.com");
			helper.setTo(mail);
			helper.setSubject("Split it - Has realizado un pago");
			helper.setText(htmlMsg, true);
			
			javaMailSender.send(mimeMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendNotificacionPagoRecibido(String mail, String textoNotificacion) throws MailingException {
		MimeMessage mimeMsg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, "utf-8");
		String htmlMsg = "<h3>¡Hola!</h3>"
				+ "<p>" + textoNotificacion + "</p>"
				+ "<br>"
				+ "<p>¡Saludos desde el equipo de Split it! 👋</p>";
		try {
			helper.setFrom("split.itnoreplay@gmail.com");
			helper.setTo(mail);
			helper.setSubject("Split it - Has recibido un pago");
			helper.setText(htmlMsg, true);
			
			javaMailSender.send(mimeMsg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
