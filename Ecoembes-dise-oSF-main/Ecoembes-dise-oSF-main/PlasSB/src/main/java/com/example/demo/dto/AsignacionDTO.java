package com.example.demo.dto;

public class AsignacionDTO {
    private Long id;
    private Long plantaId;
    private String plantaNombre;
    private java.util.List<String> contenedoresIds;
    private java.time.LocalDate fecha;
    private int totalContenedores;
    private int totalEnvases;
    private float pesoEstimado;
    private String empleadoResponsable;
    private java.time.LocalDateTime fechaAsignacion;

    public AsignacionDTO() {
    }

    public AsignacionDTO(Long id, Long plantaId, String plantaNombre, java.util.List<String> contenedoresIds,
            java.time.LocalDate fecha, int totalContenedores, int totalEnvases, float pesoEstimado,
            String empleadoResponsable, java.time.LocalDateTime fechaAsignacion) {
        this.id = id;
        this.plantaId = plantaId;
        this.plantaNombre = plantaNombre;
        this.contenedoresIds = contenedoresIds;
        this.fecha = fecha;
        this.totalContenedores = totalContenedores;
        this.totalEnvases = totalEnvases;
        this.pesoEstimado = pesoEstimado;
        this.empleadoResponsable = empleadoResponsable;
        this.fechaAsignacion = fechaAsignacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(Long plantaId) {
        this.plantaId = plantaId;
    }

    public String getPlantaNombre() {
        return plantaNombre;
    }

    public void setPlantaNombre(String plantaNombre) {
        this.plantaNombre = plantaNombre;
    }

    public java.util.List<String> getContenedoresIds() {
        return contenedoresIds;
    }

    public void setContenedoresIds(java.util.List<String> contenedoresIds) {
        this.contenedoresIds = contenedoresIds;
    }

    public java.time.LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(java.time.LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getTotalContenedores() {
        return totalContenedores;
    }

    public void setTotalContenedores(int totalContenedores) {
        this.totalContenedores = totalContenedores;
    }

    public int getTotalEnvases() {
        return totalEnvases;
    }

    public void setTotalEnvases(int totalEnvases) {
        this.totalEnvases = totalEnvases;
    }

    public float getPesoEstimado() {
        return pesoEstimado;
    }

    public void setPesoEstimado(float pesoEstimado) {
        this.pesoEstimado = pesoEstimado;
    }

    public String getEmpleadoResponsable() {
        return empleadoResponsable;
    }

    public void setEmpleadoResponsable(String empleadoResponsable) {
        this.empleadoResponsable = empleadoResponsable;
    }

    public java.time.LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(java.time.LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}
