package com.splitit.splitit.backend.Security;

import java.util.Collections;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DAO.UsuarioAdministrador;
import com.splitit.splitit.backend.Repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	private UsuarioRepository usuarioRepo;

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		Optional<Usuario> usuario = Optional.of(usuarioRepo.findByEmail(email));
		if(usuario.isEmpty()) {
			throw new UsernameNotFoundException("Could not find User with email = " + email);
		}
		Usuario usuarioObj = usuario.get();
		String rol;
		if(usuarioObj instanceof UsuarioAdministrador) {
			rol = "ROLE_ADMINISTRADOR";
		}else {
			rol = "ROLE_USUARIO";
		}
		return new org.springframework.security.core.userdetails.User(
				email, 
				usuarioObj.getContrasena(), 
				Collections.singletonList(new SimpleGrantedAuthority(rol)));
	}
}
