package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.splitit.splitit.backend.DAO.UsuarioFinal;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface UsuarioFinalRepository extends JpaRepository<UsuarioFinal, Long>{

}
