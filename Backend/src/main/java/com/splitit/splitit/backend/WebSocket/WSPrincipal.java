package com.splitit.splitit.backend.WebSocket;

import java.security.Principal;
import java.util.Objects;

public class WSPrincipal implements Principal{
	private String name;
	private int id;
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object another) {
		if(!(another instanceof Principal)) return false;
		WSPrincipal principal = (WSPrincipal) another;
		return principal.getId() == this.id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
