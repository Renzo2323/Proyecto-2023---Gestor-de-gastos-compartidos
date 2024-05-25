package com.splitit.splitit.backend.WebSocket;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Controller
@CrossOrigin
public class NotificacionController {
	private SimpMessagingTemplate template;
	
	@Autowired
	public NotificacionController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@MessageMapping("/test")
	@SendTo("/topic/broadcast")
	public String handle(@Payload String mensajeCliente) throws Exception {
//		StompHeaderAccessor sha;
//		sha.get
		return "El usuario envio el siguiente mensaje: " + HtmlUtils.htmlEscape(mensajeCliente);

	}
	
	@MessageMapping("/teset")
	@SendTo("/topic/1")
	public String handleUnico(@Payload String mensajeCliente) throws Exception {
		return "Este mensaje es solo para ti";

	}
	
	@SendTo("/topic/broadcast")
	public void enviarBroadcast(String mensaje) throws Exception {
		template.convertAndSend("/topic/broadcast", mensaje);

	}
	
//	@SendTo("/topic/broadcast")
	public void enviarUsuario(String mensaje, int id) throws Exception {
		String topic = "/topic/" + String.valueOf(id);
		template.convertAndSend(topic, mensaje);

	}
	
	
}
