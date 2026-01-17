package com.example.demo.dto;

import java.time.OffsetDateTime;
import java.util.Objects;

public class ContenedorDTO {

    public enum NivelLlenado {
        VERDE, NARANJA, ROJO
    }

    private String id;
    private String ubicacion;
    private String codigoPostal;
    private float capacidadTotal;
    private NivelLlenado nivelLlenado;
    private int numEnvases;
    private OffsetDateTime ultimaActualizacion;
    private String nombrePlanta;

    public ContenedorDTO() {
    }

    // Constructor para crear nuevo contenedor
    public ContenedorDTO(String id, String ubicacion, String codigoPostal, float capacidadTotal) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.codigoPostal = codigoPostal;
        this.capacidadTotal = capacidadTotal;
        this.nivelLlenado = NivelLlenado.VERDE;
        this.numEnvases = 0;
        this.ultimaActualizacion = OffsetDateTime.now();
    }

    // Constructor completo
    public ContenedorDTO(String id, String ubicacion, String codigoPostal, float capacidadTotal,
            NivelLlenado nivelLlenado, int numEnvases, OffsetDateTime ultimaActualizacion, String nombrePlanta) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.codigoPostal = codigoPostal;
        this.capacidadTotal = capacidadTotal;
        this.nivelLlenado = nivelLlenado;
        this.numEnvases = numEnvases;
        this.ultimaActualizacion = ultimaActualizacion;
        this.nombrePlanta = nombrePlanta;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public float getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(float capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public NivelLlenado getNivelLlenado() {
        return nivelLlenado;
    }

    public void setNivelLlenado(NivelLlenado nivelLlenado) {
        this.nivelLlenado = nivelLlenado;
    }

    public int getNumEnvases() {
        return numEnvases;
    }

    public void setNumEnvases(int numEnvases) {
        this.numEnvases = numEnvases;
    }

    public OffsetDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(OffsetDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getNombrePlanta() {
        return nombrePlanta;
    }

    public void setNombrePlanta(String nombrePlanta) {
        this.nombrePlanta = nombrePlanta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContenedorDTO other = (ContenedorDTO) obj;
        return Objects.equals(id, other.id);
    }
}