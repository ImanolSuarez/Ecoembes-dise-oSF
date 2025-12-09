package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entidad Asignacion - Representa la asignación de contenedores a una planta de reciclaje
 */
@Entity
@Table(name = "asignaciones")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La planta no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "planta_id", nullable = false)
    private PlantaReciclaje planta;

    @ElementCollection
    @CollectionTable(name = "asignacion_contenedores", joinColumns = @JoinColumn(name = "asignacion_id"))
    @Column(name = "contenedor_id")
    private List<String> contenedoresIds = new ArrayList<>();

    @NotNull(message = "La fecha no puede ser nula")
    @Future(message = "La fecha debe ser futura")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Positive(message = "El total de contenedores debe ser positivo")
    @Column(name = "total_contenedores", nullable = false)
    private int totalContenedores;

    @Positive(message = "El total de envases debe ser positivo")
    @Column(name = "total_envases", nullable = false)
    private int totalEnvases;

    @Positive(message = "El peso estimado debe ser positivo")
    @Column(name = "peso_estimado", nullable = false)
    private float pesoEstimado; // En toneladas

    @ManyToOne
    @JoinColumn(name = "empleado_email", referencedColumnName = "email", nullable = false)
    private Personal empleadoResponsable;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    // Constructor sin parámetros
    public Asignacion() {
    }

    // Constructor con parámetros
    public Asignacion(PlantaReciclaje planta, List<String> contenedoresIds, LocalDate fecha,
                     int totalContenedores, int totalEnvases, float pesoEstimado, Personal empleadoResponsable) {
        this.planta = planta;
        this.contenedoresIds = contenedoresIds;
        this.fecha = fecha;
        this.totalContenedores = totalContenedores;
        this.totalEnvases = totalEnvases;
        this.pesoEstimado = pesoEstimado;
        this.empleadoResponsable = empleadoResponsable;
        this.fechaAsignacion = LocalDateTime.now();
    }

    // Constructor completo
    public Asignacion(Long id, PlantaReciclaje planta, List<String> contenedoresIds, LocalDate fecha,
                     int totalContenedores, int totalEnvases, float pesoEstimado, Personal empleadoResponsable) {
        this.id = id;
        this.planta = planta;
        this.contenedoresIds = contenedoresIds;
        this.fecha = fecha;
        this.totalContenedores = totalContenedores;
        this.totalEnvases = totalEnvases;
        this.pesoEstimado = pesoEstimado;
        this.empleadoResponsable = empleadoResponsable;
        this.fechaAsignacion = LocalDateTime.now();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlantaReciclaje getPlanta() {
        return planta;
    }

    public void setPlanta(PlantaReciclaje planta) {
        this.planta = planta;
    }

    public List<String> getContenedoresIds() {
        return contenedoresIds;
    }

    public void setContenedoresIds(List<String> contenedoresIds) {
        this.contenedoresIds = contenedoresIds;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    public Personal getEmpleadoResponsable() {
        return empleadoResponsable;
    }

    public void setEmpleadoResponsable(Personal empleadoResponsable) {
        this.empleadoResponsable = empleadoResponsable;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    // Métodos de negocio
    
    /**
     * Añade un contenedor a la asignación
     */
    public void addContenedor(String contenedorId) {
        if (!contenedoresIds.contains(contenedorId)) {
            contenedoresIds.add(contenedorId);
        }
    }

    /**
     * Calcula el peso estimado en toneladas (1 envase ≈ 0.02 kg = 0.00002 toneladas)
     */
    public static float calcularPesoEstimado(int numEnvases) {
        return (numEnvases * 0.02f) / 1000f; // Convertir a toneladas
    }

    /**
     * Verifica si la asignación está completada
     */
    public boolean isCompletada() {
        return LocalDate.now().isAfter(fecha);
    }

    // hashCode y equals
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Asignacion other = (Asignacion) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "id=" + id +
                ", plantaId=" + (planta != null ? planta.getId() : null) +
                ", fecha=" + fecha +
                ", totalContenedores=" + totalContenedores +
                ", totalEnvases=" + totalEnvases +
                ", pesoEstimado=" + pesoEstimado +
                '}';
    }
}