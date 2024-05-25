package com.splitit.splitit.backend.ServicesImplementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.Parametro;
import com.splitit.splitit.backend.DTO.DtParametro;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.Repository.ParametroRepository;
import com.splitit.splitit.backend.Services.ParametroService;

@Service
public class ParametroServiceImplementation implements ParametroService {
	
	@Autowired
	private ParametroRepository parametroRepo;

	@Override
	public List<DtParametro> getParametros() {
		List<DtParametro> listaDTParametros = new ArrayList<DtParametro>();
		
		List<Parametro> listaParametros = getAllParametros();
		for (Parametro parametro : listaParametros) {
			DtParametro DTParametro = new DtParametro();
			DTParametro.setId(parametro.getId());
			DTParametro.setNombre(parametro.getNombre());
			DTParametro.setValor(parametro.getValor());
			
			listaDTParametros.add(DTParametro);
		}
		
		return listaDTParametros;
	}
	
	private List<Parametro> getAllParametros(){
		return parametroRepo.findAll();
	}

	@Override
	public DtResponse crearParametro(DtParametro dtParametro) {
		DtResponse respuesta = new DtResponse("Error al crear el parametro", false);
		Parametro nuevoParametro = new Parametro(dtParametro.getNombre(), dtParametro.getValor());
		
		parametroRepo.save(nuevoParametro);
		respuesta = new DtResponse("Parametro creado con exito", true);
		
		return respuesta;
	}

	@Override
	public DtResponse modificarParametro(DtParametro dtParametro) {
		DtResponse respuesta = new DtResponse("Error al modificar el parametro", false);
		Optional<Parametro> parametroOpt = parametroRepo.findById(dtParametro.getId());
		
		if (parametroOpt.isPresent()) {
			Parametro parametro = parametroOpt.get();
			
			parametro.setNombre(dtParametro.getNombre());
			parametro.setValor(dtParametro.getValor());
			
			parametroRepo.save(parametro);
			
			respuesta = new DtResponse("Parametro modificado exitosamente", true);
		}
		else {
			respuesta = new DtResponse("No existe parametro con ese id", false);
		}
		return respuesta;
	}

	@Override
	public DtResponse eliminarParametro(int parametroId) {
		DtResponse respuesta = new DtResponse("No se pudo eliminar el parametro", false);
		Optional<Parametro> parametroOpt = parametroRepo.findById(parametroId);
		
		if(parametroOpt.isPresent()) {
			parametroRepo.delete(parametroOpt.get());
			respuesta = new DtResponse("Parametro eliminado con exito", true);
		}
		else {
			respuesta = new DtResponse("No existe parametro con ese id", false);
		}
		return respuesta;
	}
	
	@Override
	public DtResponse reemplazarParametros(List<DtParametro> parametros) {
		parametroRepo.deleteAll();
		List<Parametro> listaParametros = new ArrayList<Parametro>();
		for (DtParametro dtP : parametros) {
			Parametro nuevoParametro = new Parametro(dtP.getNombre(), dtP.getValor());
			listaParametros.add(nuevoParametro);
		}
		parametroRepo.saveAll(listaParametros);
		return new DtResponse("Parametros reemplazados con exito", true);
	}

}
