package com.uniajc.hotellosolivos.model;

public class Huesped {

    private int huespedId;

    private String cedula;

    private String nombre;

    private String telefono;

    private String tipoHuesped;

    private String estado;

    public Huesped() {
    }

    public int getHuespedId() {
        return huespedId;
    }

    public void setHuespedId(int huespedId) {
        this.huespedId = huespedId;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoHuesped() {
        return tipoHuesped;
    }

    public void setTipoHuesped(String tipoHuesped) {
        this.tipoHuesped = tipoHuesped;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
