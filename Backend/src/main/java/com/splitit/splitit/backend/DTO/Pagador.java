package com.splitit.splitit.backend.DTO;

public class Pagador {
	
	private int avatar;
	private float pago;
	
	public Pagador() {}
	
	public Pagador(int avatar, float pago) {
		super();
		this.avatar = avatar;
		this.pago = pago;
	}
	
	public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public float getPago() {
        return pago;
    }

    public void setPago(float pago) {
        this.pago = pago;
    }
}
