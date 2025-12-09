package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asignacionId;

    @Column(nullable = false)
    private LocalDateTime fechaNotificacion;

    @Column(nullable = false)
    private float pesoEstimado;

    @Column(nullable = false)
    private boolean procesada = false;

    public Asignacion() {
    }

    public Asignacion(String asignacionId, LocalDateTime fechaNotificacion, float pesoEstimado) {
        this.asignacionId = asignacionId;
        this.fechaNotificacion = fechaNotificacion;
        this.pesoEstimado = pesoEstimado;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsignacionId() {
        return asignacionId;
    }

    public void setAsignacionId(String asignacionId) {
        this.asignacionId = asignacionId;
    }

    public LocalDateTime getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(LocalDateTime fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public float getPesoEstimado() {
        return pesoEstimado;
    }

    public void setPesoEstimado(float pesoEstimado) {
        this.pesoEstimado = pesoEstimado;
    }

    public boolean isProcesada() {
        return procesada;
    }

    public void setProcesada(boolean procesada) {
        this.procesada = procesada;
    }
}
