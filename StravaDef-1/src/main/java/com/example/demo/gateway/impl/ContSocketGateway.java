package com.example.demo.gateway.impl;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.dto.PlantaReciclajeDTO;
import com.example.demo.dto.RecepcionDTO;
import com.example.demo.gateway.IPlantaReciclajeGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ContSocketGateway implements IPlantaReciclajeGateway {

    private static final Logger logger = LoggerFactory.getLogger(ContSocketGateway.class);

    private final String host;
    private final int port;
    private final String nombre;

    public ContSocketGateway(String host, int port, String nombre) {
        this.host = host;
        this.port = port;
        this.nombre = nombre;
    }

    private String sendCommand(String command) {
        try (Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);
            return in.readLine();
        } catch (Exception e) {
            logger.error("Error comunicando con ContSocket: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public PlantaReciclajeDTO obtenerInformacionPlanta() {
        return new PlantaReciclajeDTO(nombre, "Ubicacion ContSocket", 800.0f);
    }

    @Override
    public CapacidadPlantaDTO consultarCapacidadDisponible(LocalDate fecha) {
        String response = sendCommand("CAPACITY:" + fecha.toString());
        if (response != null && response.startsWith("CAPACITY:")) {
            String[] parts = response.split(":");
            if (parts.length >= 4) {
                float total = Float.parseFloat(parts[1]);
                float available = Float.parseFloat(parts[2]);
                float occupied = Float.parseFloat(parts[3]);
                return new CapacidadPlantaDTO(fecha, total, available, occupied);
            }
        }
        return new CapacidadPlantaDTO(fecha, 0, 0, 0);
    }

    @Override
    public boolean notificarAsignacion(AsignacionDTO asignacion) {
        // Serializar asignacion a JSON o formato simple
        String command = "ASSIGN:" + asignacion.getId(); // Simplificado
        String response = sendCommand(command);
        return "OK".equals(response);
    }

    @Override
    public RecepcionDTO registrarRecepcion(RecepcionDTO recepcion) {
        // No implementado para socket en este ejemplo
        return null;
    }

    @Override
    public List<RecepcionDTO> obtenerHistorialRecepciones(LocalDate fechaInicio, LocalDate fechaFin) {
        return Collections.emptyList();
    }

    @Override
    public boolean verificarDisponibilidad() {
        String response = sendCommand("PING");
        return "PONG".equals(response);
    }

    @Override
    public String getNombrePlanta() {
        return nombre;
    }

    @Override
    public String getTipoComunicacion() {
        return "SOCKET";
    }
}
