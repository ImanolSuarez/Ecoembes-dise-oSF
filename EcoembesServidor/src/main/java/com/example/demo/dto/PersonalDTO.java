package com.example.demo.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalDTO {
	
	private Long token;
	private String nombre;
	private String email;
	private LocalDate fechaAlta;
	
	// Lista simplificada de asignaciones realizadas
	private List<AsignacionSimpleDTO> asignacionesRealizadas;
	
	// DTO interno simplificado para evitar referencias circulares
	public static class AsignacionSimpleDTO {
		private Long id;
		private Long plantaId;
		private LocalDate fecha;
		private int totalContenedores;
		
		public AsignacionSimpleDTO() {}
		
		public AsignacionSimpleDTO(Long id, Long plantaId, LocalDate fecha, int totalContenedores) {
			this.id = id;
			this.plantaId = plantaId;
			this.fecha = fecha;
			this.totalContenedores = totalContenedores;
		}
		
		public Long getId() { return id; }
		public void setId(Long id) { this.id = id; }
		public Long getPlantaId() { return plantaId; }
		public void setPlantaId(Long plantaId) { this.plantaId = plantaId; }
		public LocalDate getFecha() { return fecha; }
		public void setFecha(LocalDate fecha) { this.fecha = fecha; }
		public int getTotalContenedores() { return totalContenedores; }
		public void setTotalContenedores(int totalContenedores) { this.totalContenedores = totalContenedores; }
	}
	
	public PersonalDTO() {}

	// Constructor sin asignaciones
	public PersonalDTO(String nombre, String email, LocalDate fechaAlta) {
		this.nombre = nombre;
		this.email = email;
		this.fechaAlta = fechaAlta;
		this.asignacionesRealizadas = new ArrayList<>();
	}
	
	// Constructor con token
	public PersonalDTO(Long token, String nombre, String email, LocalDate fechaAlta) {
		this.token = token;
		this.nombre = nombre;
		this.email = email;
		this.fechaAlta = fechaAlta;
		this.asignacionesRealizadas = new ArrayList<>();
	}
	
	// Getters y Setters
	public Long getToken() {
		return token;
	}

	public void setToken(Long token) {
		this.token = token;
	}

	public String getNombre() {
		return nombre;
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

	public List<AsignacionSimpleDTO> getAsignacionesRealizadas() {
		return asignacionesRealizadas;
	}

	public void setAsignacionesRealizadas(List<AsignacionSimpleDTO> asignacionesRealizadas) {
		this.asignacionesRealizadas = asignacionesRealizadas;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonalDTO other = (PersonalDTO) obj;
		return Objects.equals(email, other.email);
	}		
}