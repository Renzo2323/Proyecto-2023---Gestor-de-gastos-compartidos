package com.splitit.splitit.backend.DTO;

import com.splitit.splitit.backend.DAO.TipoMoneda;

public class DtSaldo {
	
	private DtAvatar deudor;
	private DtAvatar acreedor;
	private float monto;
	private TipoMoneda moneda;
	
	public DtSaldo() {}
	
	public DtSaldo(DtAvatar deudor, DtAvatar acreedor, float monto, TipoMoneda moneda) {
		super();
        this.deudor = deudor;
        this.acreedor = acreedor;
        this.monto = monto;
        this.moneda = moneda;
    }

    public DtAvatar getDeudor() {
        return deudor;
    }

    public void setDeudor(DtAvatar deudor) {
        this.deudor = deudor;
    }

    public DtAvatar getAcreedor() {
        return acreedor;
    }

    public void setAcreedor(DtAvatar acreedor) {
        this.acreedor = acreedor;
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

}
