package com.proyecto.modelo;

public class Cuenta {
    private int identificacion;
    private String tipocuenta;

    public Cuenta() {

    }
    public Cuenta(int identificacion, String tipocuenta) {
        this.identificacion = identificacion;
        this.tipocuenta = tipocuenta;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(String tipocuenta) {
        this.tipocuenta = tipocuenta;
    }

    @Override
    public String toString() {
        return String.format("Cuenta [identificacion: %d, tipocuenta: %s] ", identificacion, tipocuenta);
    }
}
