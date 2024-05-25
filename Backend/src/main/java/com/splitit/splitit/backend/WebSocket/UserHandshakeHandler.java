package com.splitit.splitit.backend.WebSocket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import jakarta.servlet.http.HttpServletRequest;

public class UserHandshakeHandler extends DefaultHandshakeHandler{

	@Autowired
    private JWTUtil jwtUtil;
	
	@Autowired
    private UsuarioRepository usuarioRepository;

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		final String randomId = UUID.randomUUID().toString();
		Principal principal = request.getPrincipal();
		//Principal principal2 = ((HttpServletRequest)request).getUserPrincipal();
//		
		if(principal == null) {
			principal = new WSPrincipal();
			String uniqueName = UUID.randomUUID().toString();
			String email = "lalalla";
			if(SecurityContextHolder.getContext().getAuthentication() != null) {
				email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			((WSPrincipal) principal).setName(email);

		}

		try {
			System.out.print(request.getBody());
			InputStream a = request.getBody();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return principal;
		
	}
}
