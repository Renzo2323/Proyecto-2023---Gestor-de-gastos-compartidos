package com.splitit.splitit.backend.DTO;

public class DtSaldoSimplificado {
	private int deudor;
	private int acreedor;
	private double monto;
	
	public DtSaldoSimplificado() {}
	
	public DtSaldoSimplificado(int deudor, int acreedor, double monto) {
		super();
		this.deudor = deudor;
		this.acreedor = acreedor;
		this.monto = monto;
	}
	
	public int getDeudor() {
        return deudor;
    }

    public void setDeudor(int deudor) {
        this.deudor = deudor;
    }

    public int getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(int acreedor) {
        this.acreedor = acreedor;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    @Override
    public String toString() {
    	return "Deudor: " + deudor + ", Acreedor: " + acreedor + ", Monto: " + monto;
    }

}
