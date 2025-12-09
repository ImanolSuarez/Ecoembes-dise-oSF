package com.example.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CapacidadPlanta {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private float capacidadTotal;
    private float capacidadDisponible;
    private float ocupacionActual;

    public CapacidadPlanta() {
    }

    public CapacidadPlanta(LocalDate fecha, float capacidadTotal, float capacidadDisponible, float ocupacionActual) {
        this.fecha = fecha;
        this.capacidadTotal = capacidadTotal;
        this.capacidadDisponible = capacidadDisponible;
        this.ocupacionActual = ocupacionActual;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public float getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(float capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public float getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(float capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    public float getOcupacionActual() {
        return ocupacionActual;
    }

    public void setOcupacionActual(float ocupacionActual) {
        this.ocupacionActual = ocupacionActual;
    }
}
