package com.splitit.splitit.backend.DAO;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class GrupoDeGastos {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private String nombre;
	
	@Enumerated(EnumType.STRING)
	private TipoGrupo tipoGrupo;
	@Column(columnDefinition = "LONGTEXT")
	private String imagen;
	
	@Enumerated(EnumType.STRING)
	private TipoMoneda moneda;
	
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<CategoriaGrupo> categoriasGrupo;
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<Pago> pagos;
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<GastoCompartido> gastosCompartidos;
	
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<Avatar> avatares;
	
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<AuditoriaGrupo> auditoriaGrupo;
	
	@OneToMany(mappedBy="grupoGastos", orphanRemoval = true)
	private List<NotificacionUsuario> NotificacionesUsuario;
	
	public GrupoDeGastos() {
		super();
	}
	
	public GrupoDeGastos(int id, String nombre, TipoGrupo tipoGrupo, String imagen, TipoMoneda moneda) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoGrupo = tipoGrupo;
		this.imagen = imagen;
		this.moneda = moneda;			
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
	
	/*List<CategoriaGrupo> categoriasGrupo;*/
	public List<CategoriaGrupo> getCategoriasGrupo(){
		return categoriasGrupo;
	}
	
	public void addCategoriaGrupo(CategoriaGrupo categoria) {
		categoriasGrupo.add(categoria);
	}
	
	public List<Pago> getPagosGrupo(){
		return pagos;
	}
	
	public void addPagoGrupo(Pago pago) {
		pagos.add(pago);
	}
	
	public List<GastoCompartido> getGastosCompartidos(){
		return gastosCompartidos;
	}
	
	public void addGastoCompartido(GastoCompartido gastoCompartido) {
		gastosCompartidos.add(gastoCompartido);
	}
	
	public List<Avatar> getAvataresDeGrupo(){
		return avatares;
	}
	
	public void addAvatarAGrupo(Avatar avatar) {
		avatares.add(avatar);
	}
	
	public void addAuditoria(AuditoriaGrupo auditoria) {
		System.out.println("AGREGAR AUDITORIA");
		auditoriaGrupo.add(auditoria);
	}
	
	public List<AuditoriaGrupo> getAuditoria(){
		return auditoriaGrupo; 
	}

	public String getMonedaSimbolo() {
		switch (moneda) {
			case UYU : {
				return "$U";
						
			}
			case USD : {
				return "$";
						
			}
			case ARS : {
				return "$";
						
			}
			case GBP : {
				return "£";
						
			}
			case EUR : {
				return "€";
						
			}
			case JPY : {
				return "¥";
						
			}default : {
				return "$";
			}
		}
	}
	
	public boolean tieneAvataresVinculados() {
		for(Avatar avatar : avatares) {
			if(avatar.getUsuario() != null) {
				return true;
			}
		}
		return false;
	}

}
