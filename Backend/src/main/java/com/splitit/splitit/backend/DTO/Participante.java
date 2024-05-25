package com.splitit.splitit.backend.DTO;

public class Participante {
	
	private int avatar;
	private float cuotaParticipacion;
	
	public Participante(){}
	
	public Participante(int avatar, float cuotaParticipacion) {
		super();
		this.avatar = avatar;
		this.cuotaParticipacion = cuotaParticipacion;
	}
	
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	public void setCuotaParticipacion(float cuota) {
		this.cuotaParticipacion = cuota;
	}
	
	public int getAvatar() {
		return avatar;
	}
	
	public float getCuotaParticipacion() {
		return cuotaParticipacion;
	}
	
	

}
