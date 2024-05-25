package com.splitit.splitit.backend.Security;

import java.util.Collection;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {
	@Value("${jwt_secret}")
	private String secret;
	
	public String generateToken(String email, String nombre, Boolean admin) throws IllegalArgumentException, JWTCreationException {
		return JWT.create()
				.withSubject(email)
				.withClaim("nombre", nombre)
				.withClaim("admin", admin)
				.withIssuedAt(new Date())
				.withIssuer("SplitIt")
				.sign(Algorithm.HMAC256(secret));
	}
	
	public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withIssuer("SplitIt")
				.build();
		DecodedJWT jwt = verifier.verify(token);
		//return jwt.getClaim("email").asString();
		return jwt.getSubject();
	}
	public String getRol() {
		
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			Collection<SimpleGrantedAuthority> roles = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			System.out.print(roles);
			
			if(roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_USUARIO"))) {
				return "Usuario";
			}else if(roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMINISTRADOR"))){
				return "Administrador";
			}else {
				return "Invitado";
			}
		}
		return "Invitado";
		
	}
	public String getEmail() {
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return email;

		}
		return null;
	}
}
