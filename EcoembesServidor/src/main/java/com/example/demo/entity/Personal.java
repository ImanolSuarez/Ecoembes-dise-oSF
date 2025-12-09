package com.example.demo.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * Entidad Personal - Representa un empleado de Ecoembes
 */
@Entity
@Table(name = "personal") 
public class Personal {


    @Column(nullable = true, unique = true)
    private Long token; 

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Id
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotNull(message = "La fecha de alta no puede ser nula")
    @PastOrPresent(message = "La fecha de alta debe ser pasada o presente")
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @OneToMany(mappedBy = "empleadoResponsable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignacionesRealizadas = new ArrayList<>();

    // Constructor sin parámetros (requerido por JPA)
    public Personal() {
    }

    // Constructor con parámetros
    public Personal(String nombre, String email, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.fechaAlta = fechaAlta;
        this.asignacionesRealizadas = new ArrayList<>();
    }

    // Getters y setters
    
    public String getNombre() {
        return nombre;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public List<Asignacion> getAsignacionesRealizadas() {
        return asignacionesRealizadas;
    }

    public void setAsignacionesRealizadas(List<Asignacion> asignacionesRealizadas) {
        this.asignacionesRealizadas = asignacionesRealizadas;
    }

    // Método de conveniencia para añadir asignaciones
    public void addAsignacion(Asignacion asignacion) {
        asignacionesRealizadas.add(asignacion);
        asignacion.setEmpleadoResponsable(this);
    }

    public void removeAsignacion(Asignacion asignacion) {
        asignacionesRealizadas.remove(asignacion);
        asignacion.setEmpleadoResponsable(null);
    }

    // hashCode y equals
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Personal other = (Personal) obj;
        return Objects.equals(email, other.email);
    }

    @Override
    public String toString() {
        return "Personal{" +
                "email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", token=" + token +
                '}';
    }
}