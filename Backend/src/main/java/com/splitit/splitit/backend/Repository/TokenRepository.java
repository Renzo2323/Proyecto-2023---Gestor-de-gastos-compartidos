package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.splitit.splitit.backend.DAO.Reset_Token;
import com.splitit.splitit.backend.DAO.Usuario;

@EnableJpaRepositories
@Repository
public interface TokenRepository extends JpaRepository<Reset_Token, Integer>{
	Reset_Token findByToken(String token);
	Reset_Token findByUsuario(Usuario usuario);
}
