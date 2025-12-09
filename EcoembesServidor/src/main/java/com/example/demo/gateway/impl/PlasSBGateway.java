package com.example.demo.gateway.impl;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.dto.PlantaReciclajeDTO;
import com.example.demo.dto.RecepcionDTO;
import com.example.demo.gateway.IPlantaReciclajeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class PlasSBGateway implements IPlantaReciclajeGateway {

    private static final Logger logger = LoggerFactory.getLogger(PlasSBGateway.class);

    private final String host;
    private final int port;
    private final String nombre;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public PlasSBGateway(String host, int port, String nombre) {
        this.host = host;
        this.port = port;
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
            restTemplate.postForObject(url, asignacion, Void.class);
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
