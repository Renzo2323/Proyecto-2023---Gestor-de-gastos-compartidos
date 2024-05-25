package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.Parametro;

public interface ParametroRepository extends JpaRepository<Parametro, Integer> {
	Parametro getByNombre(String nombre);

}
