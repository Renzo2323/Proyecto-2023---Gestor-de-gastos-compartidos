package com.splitit.splitit.backend.ServicesImplementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitit.splitit.backend.DAO.AuditoriaAccion;
import com.splitit.splitit.backend.DAO.Avatar;
import com.splitit.splitit.backend.DAO.Categoria;
import com.splitit.splitit.backend.DAO.CategoriaGlobal;
import com.splitit.splitit.backend.DAO.CategoriaGrupo;
import com.splitit.splitit.backend.DAO.GrupoDeGastos;
import com.splitit.splitit.backend.DAO.Usuario;
import com.splitit.splitit.backend.DTO.DtCategoria;
import com.splitit.splitit.backend.DTO.DtGrupoDeGastos;
import com.splitit.splitit.backend.DTO.DtResponse;
import com.splitit.splitit.backend.DTO.DtUsuario;
import com.splitit.splitit.backend.Repository.CategoriaRespository;
import com.splitit.splitit.backend.Services.AuditoriaService;
import com.splitit.splitit.backend.Services.CategoriaService;
import com.splitit.splitit.backend.Repository.GrupoRepository;
import com.splitit.splitit.backend.Repository.UsuarioRepository;
import com.splitit.splitit.backend.Security.JWTUtil;

@Service
public class CategoriaServiceImplementation implements CategoriaService {
	
	@Autowired
	private CategoriaRespository categoriaRespository;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
    @Autowired
    private JWTUtil jwtUtil;
    
    private AuditoriaService auditoriaService;
	
	@Autowired
	public CategoriaServiceImplementation(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}
	
	@Override
	public DtResponse addCategoriaGlobal(DtCategoria dtCategoria){
		DtResponse dtResponse =  new DtResponse("", false);
		dtResponse = validarDatosCategoria(dtCategoria);
    	if(!dtResponse.getEstatus()) return dtResponse;
		try {
			CategoriaGlobal Categoria = new CategoriaGlobal(
					dtCategoria.getNombre(),
					dtCategoria.getIcono(),
					dtCategoria.getDescripcion(),
					true
					);
			categoriaRespository.save(Categoria);
			
			dtResponse =  new DtResponse("Categoria agregada correctamente", true);
			
		}catch(Exception e){
			dtResponse =  new DtResponse(e.getMessage(), false);
		}

		return dtResponse;
	}
	
	@Override
	public DtResponse editCategoria(int categoriaId, DtCategoria dtCategoria) {
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<Categoria> categoria = categoriaRespository.findById(categoriaId);
		if (categoria.isPresent()) {
			Categoria categoriaObj = categoria.get();
			categoriaObj.setNombre(dtCategoria.getNombre());
			categoriaObj.setIcono(dtCategoria.getIcono());
			categoriaObj.setDescripcion(dtCategoria.getDescripcion());
			categoriaObj.setEstado(dtCategoria.getEstado());
			
			categoriaRespository.save(categoriaObj);
			
			
			dtResponse =  new DtResponse("Categoria modificada correctamente", true);
		}else {
			dtResponse =  new DtResponse("No se pudo modificar la categoria", false);
		}
				
		
		return dtResponse;
	}
	
	@Override
	public DtResponse editCategoriaGrupo(int categoriaId, DtCategoria dtCategoria) {
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<Categoria> categoria = categoriaRespository.findById(categoriaId);
		if (categoria.isPresent()) {
			Categoria categoriaObj = categoria.get();
			if(categoriaObj instanceof CategoriaGlobal) {
				return new DtResponse("No tiene acceso a editar categorias globales", false);
			}
			//No verifica que el usuario logeado pertenezca al grupo con la categoria
			categoriaObj.setNombre(dtCategoria.getNombre());
			categoriaObj.setIcono(dtCategoria.getIcono());
			categoriaObj.setDescripcion(dtCategoria.getDescripcion());
			categoriaObj.setEstado(dtCategoria.getEstado());
			
			categoriaRespository.save(categoriaObj);
			
			
			dtResponse =  new DtResponse("Categoria modificada correctamente", true);
		}else {
			dtResponse =  new DtResponse("No se pudo modificar la categoria", false);
		}
				
		
		return dtResponse;
	}
	
	@Override
	public DtResponse activarCategoria(int categoriaId, boolean activa) {
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<Categoria> categoria = categoriaRespository.findById(categoriaId);
		if (categoria.isPresent()) {
			Categoria categoriaObj = categoria.get();

			
			categoriaObj.setEstado(activa);
			categoriaRespository.save(categoriaObj);
			
			if(activa) {
				dtResponse =  new DtResponse("Categoria activada correctamente", true);
			}else {
				dtResponse =  new DtResponse("Categoria desactivada correctamente", true);
			}
		}else {
			if(activa) {
				dtResponse =  new DtResponse("No se pudo activar la categoria", false);
			}else {
				dtResponse =  new DtResponse("No se pudo desactivar la categoria", false);
			}
			
		}
		return dtResponse;
		
	}
	
	@Override
	public DtResponse activarCategoriaGrupo(int categoriaId, boolean activa){
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<Categoria> categoria = categoriaRespository.findById(categoriaId);
		if (categoria.isPresent()) {
			Categoria categoriaObj = categoria.get();
			if(categoriaObj instanceof CategoriaGlobal) {
				return new DtResponse("No tiene acceso a editar categorias globales", false);
			}
			//No verifica que el usuario logeado pertenezca al grupo con la categoria
			categoriaObj.setEstado(activa);
			categoriaRespository.save(categoriaObj);
			
			if(activa) {
				dtResponse =  new DtResponse("Categoria activada correctamente", true);
			}else {
				dtResponse =  new DtResponse("Categoria desactivada correctamente", true);
			}
		}else {
			if(activa) {
				dtResponse =  new DtResponse("No se pudo activar la categoria", false);
			}else {
				dtResponse =  new DtResponse("No se pudo desactivar la categoria", false);
			}
			
		}
		return dtResponse;
	}
	
	@Override
	public DtResponse deleteCategoria(int categoriaId) {
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<Categoria> categoria = categoriaRespository.findById(categoriaId);
		if (categoria.isPresent()) {
			Categoria categoriaObj = categoria.get();
			categoriaObj.setEstado(false);
			
			dtResponse =  new DtResponse("Categoria dada de baja correctamente", true);
		}else {
			dtResponse =  new DtResponse("No se pudo modificar la categoria", false);
		}
				
		
		return dtResponse;
	}
	
	@Override
	public List<DtCategoria> getCategorias(){
		List<Categoria> listaCategorias = categoriaRespository.findAll();
		
		List<DtCategoria> listaDTCategoria = new ArrayList<DtCategoria>();
		
		for(Categoria categoriaActual : listaCategorias) {
		
			if(categoriaActual instanceof CategoriaGlobal) {
				DtCategoria dtCategoria = new DtCategoria(categoriaActual.getId(), categoriaActual.getNombre(), categoriaActual.getIcono(), categoriaActual.getDescripcion(), categoriaActual.getEstado());
				listaDTCategoria.add(dtCategoria);			
			}
		}
		
		return listaDTCategoria;
	}

	@Override
	public DtResponse addCategoriaGrupo(int idGrupo, DtCategoria dtCategoria) {
		DtResponse dtResponse =  new DtResponse("", false);
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(idGrupo);
		if (grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			
			CategoriaGrupo categoriaGrupo = new CategoriaGrupo(
					dtCategoria.getNombre(), 
					dtCategoria.getIcono(), 
					dtCategoria.getDescripcion(), 
					true
					);
			grupoObj.addCategoriaGrupo(categoriaGrupo);
			
			categoriaGrupo.setGrupoGastos(grupoObj);
			
			categoriaRespository.save(categoriaGrupo);
			
			
			//AUDITORIA
			Usuario usuario = usuarioRepository.findByEmail(jwtUtil.getEmail());
			Avatar avatar = null;
			if (usuario != null ) {
				avatar = getAvatarDeUsuarioDeGrupo(grupoObj, usuario.getId());
			}
			
			String detalle = null;
			auditoriaService.addAuditoriaGrupo(grupoObj.getId(), AuditoriaAccion.NUEVACATEGORIAGRUPO, avatar, 0, dtCategoria.getNombre());
			
			
			dtResponse =  new DtResponse("Categoria agregada correctamente", true);
		}else {
			dtResponse =  new DtResponse("No se pudo agregar la categoria", false);
		}
				
		return dtResponse;
	}

	@Override
	public List<DtCategoria> getCategoriasDeGrupo(int idGrupo) {
		//Devuelve las colecciones del grupo junto a las colecciones globales activas;
		List<DtCategoria> listaDTCategoria = new ArrayList<DtCategoria>();
		
		Optional<GrupoDeGastos> grupo = grupoRepository.findById(idGrupo);
		
		if (grupo.isPresent()) {
			GrupoDeGastos grupoObj = grupo.get();
			//Se obtienen las categorias del grupo
			List<CategoriaGrupo> listaCategoriasGrupo = grupoObj.getCategoriasGrupo();
			for(CategoriaGrupo categoriaActual : listaCategoriasGrupo) {
				DtCategoria dtCategoria = new DtCategoria(categoriaActual.getId(), categoriaActual.getNombre(), categoriaActual.getIcono(), categoriaActual.getDescripcion(), categoriaActual.getEstado());
				listaDTCategoria.add(dtCategoria);			
			}
			
		}		
	/*	
		//Se obtienen las categorias globales
		List<Categoria> listaCategorias = categoriaRespository.findAll();
				
		for(Categoria categoriaActual : listaCategorias) {
			if(categoriaActual.getEstado() != false) {
				DtCategoria dtCategoria = new DtCategoria(categoriaActual.getId(), categoriaActual.getNombre(), categoriaActual.getIcono(), categoriaActual.getDescripcion(), categoriaActual.getEstado());
				listaDTCategoria.add(dtCategoria);			
			}
		}
	*/	
		return listaDTCategoria;
	}
	
	private DtResponse validarDatosCategoria(DtCategoria dtCategoria) {
		DtResponse respuesta =  new DtResponse("", false);
		if(dtCategoria.getNombre().isEmpty()) {
			respuesta = new DtResponse("El nombre no puede ser vacio", false);
			return respuesta;
		}
		
		
		respuesta = new DtResponse("Datos correctos", true);
		return respuesta;
	}
	
	private Avatar getAvatarDeUsuarioDeGrupo(GrupoDeGastos grupo, int usuarioId) {
		List<Avatar> avatares = grupo.getAvataresDeGrupo();
		for(Avatar avatar : avatares) {
			if(avatar.getId() == usuarioId) {
				return avatar;
			}
		}
		return null;
	}

}
