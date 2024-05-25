package com.splitit.splitit.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DTO.CantGastosPorCategoria;

import jakarta.transaction.Transactional;

@Transactional
public interface GastoCompartidoRepository extends JpaRepository<GastoCompartido, Integer>{
	@Query(value="SELECT c.nombre as Categoria, count(gc.id) as Cantidad FROM gasto_compartido as gc INNER JOIN  categoria as c on c.id = gc.categoria_id and c.dtype = 'CategoriaGlobal' GROUP BY c.nombre",
	    	   nativeQuery =true)
	    List<CantGastosPorCategoria> getCantGastosCategoria();
}
