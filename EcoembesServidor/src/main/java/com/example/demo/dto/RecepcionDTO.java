package com.example.demo.dto;

import java.time.LocalDateTime;

public class RecepcionDTO {
    private String id;
    private String plantaId;
    private String asignacionId;
    private LocalDateTime fechaRecepcion;
    private boolean aceptada;
    private String mensaje;

    public RecepcionDTO() {
    }

    public RecepcionDTO(String id, String plantaId, String asignacionId, LocalDateTime fechaRecepcion, boolean aceptada,
            String mensaje) {
        this.id = id;
        this.plantaId = plantaId;
        this.asignacionId = asignacionId;
        this.fechaRecepcion = fechaRecepcion;
        this.aceptada = aceptada;
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(String plantaId) {
        this.plantaId = plantaId;
    }

    public String getAsignacionId() {
        return asignacionId;
    }

    public void setAsignacionId(String asignacionId) {
        this.asignacionId = asignacionId;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public void setAceptada(boolean aceptada) {
        this.aceptada = aceptada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
