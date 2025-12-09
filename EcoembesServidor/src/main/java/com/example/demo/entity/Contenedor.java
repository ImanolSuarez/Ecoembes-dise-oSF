package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entidad Contenedor - Representa un contenedor de reciclaje
 */
@Entity
@Table(name = "contenedores")
public class Contenedor {

    public enum NivelLlenado {
        VERDE, NARANJA, ROJO
    }

    @Id
    @NotBlank(message = "El ID del contenedor no puede estar vacío")
    @Column(nullable = false, unique = true, length = 50)
    private String id;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Column(nullable = false, length = 255)
    private String ubicacion;

    @NotBlank(message = "El código postal no puede estar vacío")
    @Column(name = "codigo_postal", nullable = false, length = 10)
    private String codigoPostal;

    @Positive(message = "La capacidad total debe ser positiva")
    @Column(name = "capacidad_total", nullable = false)
    private float capacidadTotal;

    @NotNull(message = "El nivel de llenado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_llenado", nullable = false)
    private NivelLlenado nivelLlenado;

    @Column(name = "num_envases", nullable = false)
    private int numEnvases;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "planta_id")
    private PlantaReciclaje plantaAsignada;


    // Constructor sin parámetros
    public Contenedor() {
    }

    // Constructor para crear contenedor nuevo
    public Contenedor(String id, String ubicacion, String codigoPostal, float capacidadTotal, PlantaReciclaje plantaAsignada) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.codigoPostal = codigoPostal;
        this.capacidadTotal = capacidadTotal;
        this.nivelLlenado = NivelLlenado.VERDE;
        this.numEnvases = 0;
        this.plantaAsignada = plantaAsignada;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Constructor completo
    public Contenedor(String id, String ubicacion, String codigoPostal, float capacidadTotal,
                     NivelLlenado nivelLlenado, int numEnvases, PlantaReciclaje plantaAsignada) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.codigoPostal = codigoPostal;
        this.capacidadTotal = capacidadTotal;
        this.nivelLlenado = nivelLlenado;
        this.numEnvases = numEnvases;
        this.plantaAsignada = plantaAsignada;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y setters
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
    public PlantaReciclaje getPlantaAsignada() {
        return plantaAsignada;
    }

    public void setPlantaAsignada(PlantaReciclaje plantaAsignada) {
        this.plantaAsignada = plantaAsignada;
    }


    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    
    public void actualizarEstado(int numEnvases, NivelLlenado nivelLlenado) {
        this.numEnvases = numEnvases;
        this.nivelLlenado = nivelLlenado;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    /**
     * Calcula el porcentaje de llenado
     */
    public float calcularPorcentajeLlenado() {
        return (numEnvases / capacidadTotal) * 100;
    }

    /**
     * Determina automáticamente el nivel de llenado según el porcentaje
     */
    public void determinarNivelLlenado() {
        float porcentaje = calcularPorcentajeLlenado();
        if (porcentaje <= 50) {
            this.nivelLlenado = NivelLlenado.VERDE;
        } else if (porcentaje <= 80) {
            this.nivelLlenado = NivelLlenado.NARANJA;
        } else {
            this.nivelLlenado = NivelLlenado.ROJO;
        }
    }

    /**
     * Calcula el peso estimado en kg (aprox 0.02 kg por envase)
     */
    public float calcularPesoEstimado() {
        return numEnvases * 0.02f;
    }

    /**
     * Verifica si el contenedor necesita ser vaciado
     */
    public boolean necesitaVaciado() {
        return nivelLlenado == NivelLlenado.ROJO || nivelLlenado == NivelLlenado.NARANJA;
    }

    /**
     * Vacía el contenedor (resetea valores)
     */
    public void vaciar() {
        this.numEnvases = 0;
        this.nivelLlenado = NivelLlenado.VERDE;
        this.ultimaActualizacion = LocalDateTime.now();
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
        Contenedor other = (Contenedor) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Contenedor{" +
                "id='" + id + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", nivelLlenado=" + nivelLlenado +
                ", plantaAsignada=" + (plantaAsignada != null ? plantaAsignada.getId() : "null") +
                ", numEnvases=" + numEnvases +
                '}';
    }
}