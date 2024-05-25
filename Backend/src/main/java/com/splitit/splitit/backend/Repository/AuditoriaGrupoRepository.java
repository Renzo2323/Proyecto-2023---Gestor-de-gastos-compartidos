package com.splitit.splitit.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.splitit.splitit.backend.DAO.AuditoriaGrupo;
import com.splitit.splitit.backend.DTO.AvatarMayorDeudor;
import com.splitit.splitit.backend.DTO.GastoPorCategoriaUsuario;
import com.splitit.splitit.backend.DTO.GastoPorMesUsuario;
import com.splitit.splitit.backend.DTO.GruposPorDia;
import com.splitit.splitit.backend.DTO.RegistrosPorDia;
import com.splitit.splitit.backend.DTO.TransaccionesPorDia;

import jakarta.transaction.Transactional;

@Transactional
public interface AuditoriaGrupoRepository extends JpaRepository<AuditoriaGrupo, Integer> {
	List<AuditoriaGrupo> findAllByAvatarId(int avatarId);
	List<AuditoriaGrupo> findAllByIdEntidad(int avatarId);
	
	@Query(value="SELECT CAST(create_date AS DATE) as fecha , COUNT(accion) as cantidad FROM auditoria_grupo where accion = 'NUEVOGRUPO' group by CAST(create_date AS DATE) ORDER BY CAST(create_date AS DATE)",
	    	   nativeQuery =true)
	List<GruposPorDia> getGruposPorDia();
	
	@Query(value="SELECT CAST(create_date AS DATE) AS Fecha, count(accion) AS Cantidad FROM auditoria_grupo WHERE accion = 'NUEVOGASTO' or accion = 'NUEVOPAGO' GROUP BY CAST(create_date AS DATE) ORDER BY CAST(create_date AS DATE)",
	    	   nativeQuery =true)
	List<TransaccionesPorDia> getTransaccionesPorDia();
	
	
	@Query(value="SELECT avatar.id AS Id, avatar.nombre AS Nombre, (avatar.saldo_local * -1) AS Saldo, usuario.imagen AS Imagen FROM splitit.avatar LEFT JOIN splitit.usuario on avatar.usuario_id = usuario.id WHERE grupo_gastos_id = :grupoId order by saldo_local asc limit 1",
			nativeQuery =true)
	AvatarMayorDeudor getAvatarMayorDeudor(@Param("grupoId") int grupoId);
	
	@Query(value="SELECT categoria.nombre as nombre, SUM(deuda.cuota) as montoTotal FROM splitit.gasto_compartido INNER JOIN splitit.deuda ON gasto_compartido.id = deuda.gasto_compartido_id INNER JOIN splitit.categoria ON gasto_compartido.categoria_id = categoria.id WHERE gasto_compartido.grupo_gastos_id = :grupoId AND deuda.deudor_id = :avatarId GROUP BY categoria.nombre ORDER BY SUM(deuda.cuota) DESC",
			nativeQuery = true)
	List<GastoPorCategoriaUsuario> getGastosPorCategoria(@Param("grupoId") int grupoId, @Param("avatarId") int avatarId);
	
	@Query(value="SELECT STR_TO_DATE(CONCAT('01-', datos.mes, '-', datos.anio), '%d-%m-%Y') as fecha,  datos.montoTotal FROM (SELECT YEAR(gasto_compartido.fecha) AS anio, MONTH(gasto_compartido.fecha) as mes, SUM(deuda.cuota) AS montoTotal FROM splitit.gasto_compartido INNER JOIN splitit.deuda ON gasto_compartido.id = deuda.gasto_compartido_id WHERE gasto_compartido.grupo_gastos_id = :grupoId AND deuda.deudor_id = :avatarId GROUP BY YEAR(gasto_compartido.fecha), MONTH(gasto_compartido.fecha)) as datos;",
			nativeQuery = true)
	List<GastoPorMesUsuario> getTotalGastosPorMes(@Param("grupoId") int grupoId, @Param("avatarId") int avatarId);
	
	@Query(value="SELECT CASE WHEN SUM(deuda.cuota) IS NOT NULL THEN SUM(deuda.cuota) ELSE 0 END AS montoTotal FROM splitit.gasto_compartido INNER JOIN splitit.deuda ON gasto_compartido.id = deuda.gasto_compartido_id WHERE gasto_compartido.grupo_gastos_id = :grupoId AND deuda.deudor_id = :avatarId ", 
			nativeQuery = true)
	float getTotalUsuarioPorGrupo(@Param("grupoId") int grupoId, @Param("avatarId") int avatarId);

}
