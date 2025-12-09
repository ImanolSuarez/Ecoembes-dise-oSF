package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Entidad PlantaReciclaje - Representa una planta de reciclaje externa
 * Ejemplos: PlasSB Ltd., ContSocket Ltd.
 */
@Entity
@Table(name = "plantas_reciclaje")
public class PlantaReciclaje {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Column(nullable = false, length = 255)
    private String ubicacion;

    @Positive(message = "La capacidad diaria debe ser positiva")
    @Column(name = "capacidad_diaria", nullable = false)
    private float capacidadDiaria; // En toneladas


    @Column(nullable = false)
    private boolean activa = true;

    @OneToMany(mappedBy = "planta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones = new ArrayList<>();

    @OneToMany(mappedBy = "plantaAsignada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contenedor> contenedores = new ArrayList<>();


    // Constructor sin parámetros
    public PlantaReciclaje() {
    }

    // Constructor con parámetros
    public PlantaReciclaje(String nombre, String ubicacion, float capacidadDiaria) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadDiaria = capacidadDiaria;
        this.activa = true;
        this.asignaciones = new ArrayList<>();
        this.contenedores = new ArrayList<>();
    }

    // Constructor completo
    public PlantaReciclaje(Long id, String nombre, String ubicacion, float capacidadDiaria) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadDiaria = capacidadDiaria;
        this.activa = true;
        this.asignaciones = new ArrayList<>();
        this.contenedores = new ArrayList<>();
    }

    // Getters y setters
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

    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    // Métodos de conveniencia
    public void addAsignacion(Asignacion asignacion) {
        asignaciones.add(asignacion);
        asignacion.setPlanta(this);
    }

    public void removeAsignacion(Asignacion asignacion) {
        asignaciones.remove(asignacion);
        asignacion.setPlanta(null);
    }
    public List<Contenedor> getContenedores() {
    return contenedores;
    }

    public void setContenedores(List<Contenedor> contenedores) {
        this.contenedores = contenedores;
    }

    public void addContenedor(Contenedor contenedor) {
        contenedores.add(contenedor);
        contenedor.setPlantaAsignada(this);
    }

    public void removeContenedor(Contenedor contenedor) {
        contenedores.remove(contenedor);
        contenedor.setPlantaAsignada(null);
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
        PlantaReciclaje other = (PlantaReciclaje) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "PlantaReciclaje{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidadDiaria=" + capacidadDiaria +
                ", activa=" + activa +
                '}';
    }
}