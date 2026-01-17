package com.example.demo.gateway.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.dto.PlantaReciclajeDTO;
import com.example.demo.dto.RecepcionDTO;
import com.example.demo.gateway.IPlantaReciclajeGateway;

/**
 * GATEWAY HTTP (Adapter Pattern)
 * 
 * Se conecta: Al proyecto externo vía HTTP(S)
 * Usa: Cliente HTTP (RestTemplate)
 * Responsabilidad: Enviar requests REST, recibir responses
 * Patrón: Adapter (adapta la interfaz IPlantaReciclajeGateway a la API REST
 * externa)
 * Maneja: Serialización JSON, headers, autenticación (si fuera necesaria)
 */
public class PlasSBGateway implements IPlantaReciclajeGateway {

    private static final Logger logger = LoggerFactory.getLogger(PlasSBGateway.class);

    private final String nombre;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public PlasSBGateway(String host, int port, String nombre) {
        this.nombre = nombre;
        this.baseUrl = String.format("http://%s:%d/api/v1", host, port);
        this.restTemplate = new RestTemplate();
    }

    @Override
    public PlantaReciclajeDTO obtenerInformacionPlanta() {
        // En un caso real, esto llamaría a un endpoint de la planta
        // Por ahora, devolvemos lo que sabemos
        return new PlantaReciclajeDTO(nombre, "Ubicacion PlasSB", 1000.0f);
    }

    @Override
    public CapacidadPlantaDTO consultarCapacidadDisponible(LocalDate fecha) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/capacity")
                .queryParam("date", fecha.toString())
                .toUriString();

        try {
            return restTemplate.getForObject(url, CapacidadPlantaDTO.class);
        } catch (Exception e) {
            logger.error("Error consultando capacidad en PlasSB: {}", e.getMessage());
            return new CapacidadPlantaDTO(fecha, 0, 0, 0);
        }
    }

    @Override
    public boolean notificarAsignacion(AsignacionDTO asignacion) {
        String url = baseUrl + "/asignacion";
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            org.springframework.http.HttpEntity<AsignacionDTO> request = new org.springframework.http.HttpEntity<>(
                    asignacion, headers);

            restTemplate.postForObject(url, request, Void.class);
            return true;
        } catch (Exception e) {
            logger.error("Error notificando asignacion a PlasSB: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public RecepcionDTO registrarRecepcion(RecepcionDTO recepcion) {
        String url = baseUrl + "/recepcion";
        try {
            return restTemplate.postForObject(url, recepcion, RecepcionDTO.class);
        } catch (Exception e) {
            logger.error("Error registrando recepcion en PlasSB: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<RecepcionDTO> obtenerHistorialRecepciones(LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementación simplificada
        return Collections.emptyList();
    }

    @Override
    public boolean verificarDisponibilidad() {
        String url = baseUrl + "/health";
        try {
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getNombrePlanta() {
        return nombre;
    }

    @Override
    public String getTipoComunicacion() {
        return "REST";
    }
}
