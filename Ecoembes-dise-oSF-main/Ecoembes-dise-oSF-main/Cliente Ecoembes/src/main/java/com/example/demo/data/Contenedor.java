package com.example.demo.data;

import java.time.OffsetDateTime;

public record Contenedor(
    String id,
    String ubicacion,
    String codigoPostal,
    float capacidadTotal,
    NivelLlenado nivelLlenado,
    int numEnvases,
    OffsetDateTime ultimaActualizacion,
    String nombrePlanta
) {
    
    public enum NivelLlenado {
        VERDE, NARANJA, ROJO
    }
    
    public Contenedor(String id, String ubicacion, String codigoPostal, float capacidadTotal) {
        this(id, ubicacion, codigoPostal, capacidadTotal, NivelLlenado.VERDE, 0, OffsetDateTime.now(), null);
    }
}
