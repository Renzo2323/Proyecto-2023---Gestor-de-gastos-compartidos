package com.splitit.splitit.backend.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;

import jakarta.transaction.Transactional;

@EnableJpaRepositories
@Transactional
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{
	Optional<Usuario> findOneByEmailAndContrasena(String email, String password);
    Usuario findByEmail(String email);
    
    @Query(value="SELECT CAST(u.fecha_alta AS DATE) AS Fecha, count(u.email) AS Cantidad FROM usuario as u WHERE u.dtype = 'UsuarioFinal' GROUP BY CAST(u.fecha_alta AS DATE) ORDER BY CAST(u.fecha_alta AS DATE)",
    	   nativeQuery =true)
    List<RegistrosPorDia> getRegPorDia();
    
}
