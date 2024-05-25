package com.splitit.splitit.backend.DTO;

import java.time.LocalDateTime;

public class DtNotificacionUsuario {
	private int id;
	private LocalDateTime fecha;
	private String texto;
	private boolean leida;
	
	private DtUsuario usuario;
	
	private int grupoId;
	
	public DtNotificacionUsuario() {
    }

    // Constructor con parámetros
    public DtNotificacionUsuario(int id, LocalDateTime fecha, String texto, boolean leida, DtUsuario usuario, int grupoId) {
        this.id = id;
        this.fecha = fecha;
        this.texto = texto;
        this.leida = leida;
        this.usuario = usuario;
        this.grupoId = grupoId;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public DtUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(DtUsuario usuario) {
        this.usuario = usuario;
    }

    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }
}
