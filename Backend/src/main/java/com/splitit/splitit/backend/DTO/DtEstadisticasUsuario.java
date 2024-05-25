package com.splitit.splitit.backend.DTO;

import com.splitit.splitit.backend.DAO.TipoMoneda;

import java.util.ArrayList;
import java.util.List;

public class DtEstadisticasUsuario {
	private int id;
	private String nombre;
	private float total;
	private TipoMoneda moneda;

	private List<GastoPorMesUsuario> totalGastosPorMes;
	
	private List<GastoPorCategoriaUsuario> categorias;
	
	private AvatarMayorDeudor avatarConMasDeuda;
	
	
	public DtEstadisticasUsuario() {}
	
	public DtEstadisticasUsuario(int id, String nombre, TipoMoneda moneda) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.moneda = moneda;
	}
	
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }
	
    public void setTotalGastosPorMes(List<GastoPorMesUsuario> totalGastosPorMes) {
    	this.totalGastosPorMes = totalGastosPorMes;
    }
    
    public List<GastoPorMesUsuario> getTotalGastosPorMes(){
    	return totalGastosPorMes;
    }
    
    public void setCategorias(List<GastoPorCategoriaUsuario> categorias) {
    	this.categorias = categorias;
    }
    
    public List<GastoPorCategoriaUsuario> getCategorias(){
    	return categorias;
    }
	
    public void setAvatarConMasDeuda(AvatarMayorDeudor avatar) {
    	this.avatarConMasDeuda = avatar;
    }
    
    public AvatarMayorDeudor getAvatarConMasDeuda() {
    	return avatarConMasDeuda;
    }
}
