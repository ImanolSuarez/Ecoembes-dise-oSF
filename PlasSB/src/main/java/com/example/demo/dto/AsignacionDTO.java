package com.example.demo.dto;

public class AsignacionDTO {
    private String id; // Changed from Long to String to accept UUIDs
    private float pesoEstimado;

    public AsignacionDTO() {
    }

    public AsignacionDTO(String id, float pesoEstimado) {
        this.id = id;
        this.pesoEstimado = pesoEstimado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPesoEstimado() {
        return pesoEstimado;
    }

    public void setPesoEstimado(float pesoEstimado) {
        this.pesoEstimado = pesoEstimado;
    }
}
