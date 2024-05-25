package com.splitit.splitit.backend.DTO;

import java.util.ArrayList;
import java.util.List;

import com.splitit.splitit.backend.DAO.CategoriaGrupo;
import com.splitit.splitit.backend.DAO.GastoCompartido;
import com.splitit.splitit.backend.DAO.Pago;
import com.splitit.splitit.backend.DAO.TipoGrupo;
import com.splitit.splitit.backend.DAO.TipoMoneda;


public class DtGrupoDeGastos {

	private int id;
	private String nombre;
	private TipoGrupo tipoGrupo;
	private String imagen;
	private TipoMoneda moneda;
	private float saldoLocal;
	private List<DtCategoriaGrupo> categoriasGrupo = new ArrayList<DtCategoriaGrupo>();;
	private List<DtPago> pagos = new ArrayList<DtPago>();;
	private List<DtGastoCompartido> gastosCompartidos = new ArrayList<DtGastoCompartido>();;
	private List<DtAvatar> participantes = new ArrayList<DtAvatar>();
	
	public DtGrupoDeGastos() {}
	
	public DtGrupoDeGastos(int id, String nombre, TipoGrupo tipoGrupo, String imagen, TipoMoneda moneda) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoGrupo = tipoGrupo;
		this.imagen = imagen;
		this.moneda = moneda;
	}
	
	public DtGrupoDeGastos(int id, String nombre, TipoGrupo tipoGrupo, String imagen, TipoMoneda moneda, float saldoLocal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoGrupo = tipoGrupo;
		this.imagen = imagen;
		this.moneda = moneda;
		this.saldoLocal = saldoLocal;
	}
	
	public int getId () {
		return id;		
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	 
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public TipoGrupo getTipoGrupo() {
		return tipoGrupo;
	} 
	
	public void setTipoGrupo(TipoGrupo tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public TipoMoneda getMoneda() {
		return moneda;
	}
	
	public void setMoneda(TipoMoneda moneda) {
		this.moneda = moneda;
	}
	
	public float getSaldoLocal() {
		return saldoLocal;
	}
	
	public void setSaldoLocal(float saldoLocal) {
		this.saldoLocal = saldoLocal;
	}
	
	public void addCategoriaGrupo(DtCategoriaGrupo categoria) {
		this.categoriasGrupo.add(categoria);
	}
	
	public void addPago(DtPago pago) {
		this.pagos.add(pago);
	}
	
	public void addGastoCompartido(DtGastoCompartido gasto) {
		this.gastosCompartidos.add(gasto);
	}
	
	public List<DtCategoriaGrupo> getCategoriasGrupo(){
		return categoriasGrupo;
	}
	
	public List<DtPago> getPagos(){
		return pagos;
	}
	
	public List<DtGastoCompartido> getGastosCompartidos(){
		return gastosCompartidos;
	}
	
	public List<DtAvatar> getParticipantes(){
		return participantes;
	}
	
	public void addParticipante(DtAvatar avatar) {
		this.participantes.add(avatar);
	}
	
	public void setParticipantes(List<DtAvatar> participantes) {
		this.participantes = participantes;
	}
	
	
}
