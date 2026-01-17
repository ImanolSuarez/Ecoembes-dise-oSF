package com.example.demo.dto;

import java.util.Objects;

public class PlantaReciclajeDTO {

    private Long id;
    private String nombre;
    private String ubicacion;
    private float capacidadDiaria;
    private boolean activa;

    public PlantaReciclajeDTO() {
    }

    // Constructor sin ID (para crear)
    public PlantaReciclajeDTO(String nombre, String ubicacion, float capacidadDiaria) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadDiaria = capacidadDiaria;
        this.activa = true;
    }

    // Constructor completo
    public PlantaReciclajeDTO(Long id, String nombre, String ubicacion, float capacidadDiaria,
            boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadDiaria = capacidadDiaria;
        this.activa = activa;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public float getCapacidadDiaria() {
        return capacidadDiaria;
    }

    public void setCapacidadDiaria(float capacidadDiaria) {
        this.capacidadDiaria = capacidadDiaria;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
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
        PlantaReciclajeDTO other = (PlantaReciclajeDTO) obj;
        return Objects.equals(id, other.id);
    }
}