package com.splitit.splitit.backend.DAO;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class GastoCompartido {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	private String descripcion;
	private float monto;
	private TipoMoneda moneda;
	@Column(columnDefinition = "LONGTEXT")
	private String imagen;
	
	@Enumerated(EnumType.STRING)
	private MetodoDivision metodoDivision;
	private String nota;
	private int cantidadParticipantes;
	private int cantidadPagadores;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@ManyToOne
	private Categoria categoria;
	
	@ManyToOne
	private GrupoDeGastos grupoGastos;
	
	@OneToMany(mappedBy="gastoCompartido")
	private List<Deuda> deudas;
		
	
	
	public GastoCompartido() {
		super();
	}
	
	public GastoCompartido(String descripcion, float monto, TipoMoneda moneda, String imagen, MetodoDivision metodoDivision,
			String nota, int cantidadPagadores, int cantidadParticipantes, Categoria categoria, GrupoDeGastos grupoGastos, List<Deuda> deudas) {
		super();
		this.descripcion = descripcion;
		this.monto = monto;
		this.moneda = moneda;
		this.imagen = imagen;
		this.metodoDivision = metodoDivision;
		this.nota = nota;
		this.cantidadPagadores = cantidadPagadores;
		this.cantidadParticipantes = cantidadParticipantes;
		this.categoria = categoria;
		this.grupoGastos = grupoGastos;
		this.deudas = deudas;
	}
	
	//Sin la lista de debitos y creditos
	public GastoCompartido(String descripcion, float monto, TipoMoneda moneda, String imagen, MetodoDivision metodoDivision,
			String nota, Categoria categoria, int cantidadPagadores, int cantidadParticipantes,  GrupoDeGastos grupoGastos) {
		super();
		this.descripcion = descripcion;
		this.monto = monto;
		this.moneda = moneda;
		this.imagen = imagen;
		this.metodoDivision = metodoDivision;
		this.nota = nota;
		this.categoria = categoria;
		this.cantidadPagadores = cantidadPagadores;
		this.cantidadParticipantes = cantidadParticipantes;
		this.grupoGastos = grupoGastos;
	}

	//Getter y Setters
	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public float getMonto() {
		return monto;
	}

	public TipoMoneda getMoneda() {
		return moneda;
	}
	
	public String getImagen() {
		return imagen;
	}

	public MetodoDivision getMetodoDivision() {
		return metodoDivision;
	}

	public String getNota() {
		return nota;
	}
	
	public LocalDateTime getFecha() {
		return fecha;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public GrupoDeGastos getGrupoGastos() {
		return grupoGastos;
	}
	
	public List<Deuda> getDeudas() {
		return deudas;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public void setMoneda(TipoMoneda moneda) {
		this.moneda = moneda;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	
	public void setMetodoDivision(MetodoDivision metodoDivision) {
		this.metodoDivision = metodoDivision;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setGrupoGastos(GrupoDeGastos grupoGastos) {
		this.grupoGastos = grupoGastos;
	}

	public void addDeuda(Deuda deuda) {
		this.deudas.add(deuda);
	}
	
	public int getCantidadParticipantes() {
        return cantidadParticipantes;
    }

    public void setCantidadParticipantes(int cantidadParticipantes) {
        this.cantidadParticipantes = cantidadParticipantes;
    }

    public int getCantidadPagadores() {
        return cantidadPagadores;
    }

    public void setCantidadPagadores(int cantidadPagadores) {
        this.cantidadPagadores = cantidadPagadores;
    }
}
