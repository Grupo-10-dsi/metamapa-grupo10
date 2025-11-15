package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

public class Ubicacion {
    private Double latitud;
    private Double longitud;

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }
}
