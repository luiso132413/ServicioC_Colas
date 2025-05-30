package com.proyecto.modelo;

public class Cliente {
    private int identificacion;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;

    public Cliente() {
    }

    public Cliente(int identificacion, String nombre, String apellido, String email, String telefono, String direccion) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return String.format("Cliente [identificacion: %d, nombre: %s, apellido: %s, Email: %s, Tel: %d, Dirección: %s]",
                identificacion, nombre, apellido, email, telefono, direccion);
    }
}