package com.splitit.splitit.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.splitit.splitit.backend.DAO.Avatar;

import jakarta.transaction.Transactional;

@Transactional
public interface AvatarRepository extends JpaRepository<Avatar, Integer>{

	Optional<Avatar> findByTokenInvitacion(String tokenInvitacion);

}

