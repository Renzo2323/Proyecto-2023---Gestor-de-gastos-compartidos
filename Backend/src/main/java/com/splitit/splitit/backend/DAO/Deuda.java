package com.splitit.splitit.backend.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Deuda {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	private float monto;
	private float cuota;
	
	@ManyToOne
	private Avatar deudor;
	@ManyToOne
	private Avatar acreedor;
	
	@ManyToOne
	private GastoCompartido gastoCompartido;

	
	public Deuda() {
    }

    public Deuda(float monto, float cuota, Avatar deudor, Avatar acreedor, GastoCompartido gastoCompartido) {
        this.monto = monto;
        this.cuota = cuota;
        this.deudor = deudor;
        this.acreedor = acreedor;
        this.gastoCompartido = gastoCompartido;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public float getCuota() {
        return cuota;
    }

    public void setCuota(float cuota) {
        this.cuota = cuota;
    }

    public Avatar getDeudor() {
        return deudor;
    }

    public void setDeudor(Avatar deudor) {
        this.deudor = deudor;
    }

    public Avatar getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(Avatar acreedor) {
        this.acreedor = acreedor;
    }

    public GastoCompartido getGastoCompartido() {
        return gastoCompartido;
    }

    public void setGastoCompartido(GastoCompartido gastoCompartido) {
        this.gastoCompartido = gastoCompartido;
    }
	
}
