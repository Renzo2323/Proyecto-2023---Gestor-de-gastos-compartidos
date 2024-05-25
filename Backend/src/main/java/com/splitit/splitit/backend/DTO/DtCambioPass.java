package com.splitit.splitit.backend.DTO;

public class DtCambioPass {
	private String oldPass;
	private String newPass;
	
	public DtCambioPass() {}
	
	public DtCambioPass(String oldPass, String newPass) {
		super();
		this.oldPass = oldPass;
		this.newPass = newPass;		
	}
	
	public String getOldPass() {
		return oldPass;
	}
	
	public String getNewPass() {
		return newPass;
	}
}
