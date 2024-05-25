package com.splitit.splitit.backend.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.splitit.splitit.backend.DAO.MetodoDivision;
import com.splitit.splitit.backend.DAO.TipoMoneda;




public class DtGastoCompartido {
	private int gastoId;
	private int grupoid;
	private String descripcion;
	private float monto;
	private TipoMoneda moneda;
	private String nota;
	private String imagen;
	private MetodoDivision metodoDivision;
	
	private DtCategoria categoria;
	
	private List<Pagador> pagadores;
	private List<Participante> participantes;
	

	//Constructor completo
	public DtGastoCompartido(int gastoId, int grupoid, String descripcion, float monto, TipoMoneda moneda, String nota, String imagen, DtCategoria categoria, 
			MetodoDivision metodoDivision) {
		super();
		this.gastoId = gastoId;
		this.grupoid = grupoid;
		this.descripcion = descripcion;
		this.monto = monto;
		this.moneda = moneda;
		this.nota = nota;
		this.imagen = imagen;
		this.categoria = categoria;
		this.metodoDivision = metodoDivision;
	}
	
	public int getGastoId() {
		return gastoId;
	}

	public void setGastoId(int gastoId) {
		this.gastoId = gastoId;
	}
	
	public int getGrupoid() {
        return grupoid;
    }

    public void setGrupoid(int grupoid) {
        this.grupoid = grupoid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public DtCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(DtCategoria categoria) {
        this.categoria = categoria;
    }

    public MetodoDivision getMetodoDivision() {
        return metodoDivision;
    }

    public void setMetodoDivision(MetodoDivision metodoDivision) {
        this.metodoDivision = metodoDivision;
    }

    public List<Pagador> getPagadores() {
        return pagadores;
    }

    public void setPagadores(Pagador pagador) {
        this.pagadores.add(pagador);
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Participante participante) {
        this.participantes.add(participante);
    }
    
    @JsonSetter("pagadores")
    public void setPagador(List<Pagador> pagadores) {
    	this.pagadores = pagadores;
    }
    
    @JsonSetter("participantes")
    public void setParticipantes(List<Participante> participantes) {
    	this.participantes = participantes;
    }
    
    
}
