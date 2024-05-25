package com.splitit.splitit.backend.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.Categoria;

import jakarta.transaction.Transactional;

@Transactional
public interface CategoriaRespository extends JpaRepository<Categoria,Integer>{
	
}