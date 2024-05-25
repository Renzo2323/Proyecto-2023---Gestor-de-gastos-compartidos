package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.Deuda;

import jakarta.transaction.Transactional;

@Transactional
public interface DeudaRepository extends JpaRepository<Deuda, Integer>{

}
