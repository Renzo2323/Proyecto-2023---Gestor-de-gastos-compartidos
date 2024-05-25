package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.GrupoDeGastos;

import jakarta.transaction.Transactional;

@Transactional
public interface GrupoRepository extends JpaRepository<GrupoDeGastos,Integer>{
	
}