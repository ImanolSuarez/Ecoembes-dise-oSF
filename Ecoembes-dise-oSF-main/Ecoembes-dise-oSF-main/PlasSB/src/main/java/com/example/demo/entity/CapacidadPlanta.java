package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "capacidad_planta")
public class CapacidadPlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private float capacidadTotal;

    @Column(nullable = false)
    private float capacidadDisponible;

    @Column(nullable = false)
    private float ocupacionActual;

    public CapacidadPlanta() {
    }

    public CapacidadPlanta(LocalDate fecha, float capacidadTotal, float capacidadDisponible, float ocupacionActual) {
        this.fecha = fecha;
        this.capacidadTotal = capacidadTotal;
        this.capacidadDisponible = capacidadDisponible;
        this.ocupacionActual = ocupacionActual;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
