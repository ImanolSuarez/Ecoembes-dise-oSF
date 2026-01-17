package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class AsignacionDTO {
	
	private Long id;
	private Long plantaId;
	private String plantaNombre;
	private List<String> contenedoresIds;
	private LocalDate fecha;
	private int totalContenedores;
	private int totalEnvases;
	private float pesoEstimado;
	private String empleadoResponsable;
	private LocalDateTime fechaAsignacion;
	
	public AsignacionDTO() {}
	
	// Constructor para crear asignación
	public AsignacionDTO(Long plantaId, List<String> contenedoresIds, LocalDate fecha, 
			String empleadoResponsable) {
		this.plantaId = plantaId;
		this.contenedoresIds = contenedoresIds;
		this.fecha = fecha;
		this.empleadoResponsable = empleadoResponsable;
		this.fechaAsignacion = LocalDateTime.now();
	}
	
	// Constructor completo
	public AsignacionDTO(Long id, Long plantaId, String plantaNombre, List<String> contenedoresIds, 
			LocalDate fecha, int totalContenedores, int totalEnvases, float pesoEstimado, 
			String empleadoResponsable, LocalDateTime fechaAsignacion) {
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

	// Getters y Setters
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

	public String getEmpleadoResponsable() {
		return empleadoResponsable;
	}

	public void setEmpleadoResponsable(String empleadoResponsable) {
		this.empleadoResponsable = empleadoResponsable;
	}

	public LocalDateTime getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsignacionDTO other = (AsignacionDTO) obj;
		return Objects.equals(id, other.id);
	}
}