package com.splitit.splitit.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.Pago;

import jakarta.transaction.Transactional;

@Transactional
public interface PagoRepository extends JpaRepository<Pago, Integer>{

}
