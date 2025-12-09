package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CapacidadPlanta;
import com.example.demo.service.CapacidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/capacidad")
@Tag(name = "Capacidad", description = "API para gestionar la capacidad de la planta")
public class CapacidadController {

    private final CapacidadService capacidadService;

    public CapacidadController(CapacidadService capacidadService) {
        this.capacidadService = capacidadService;
    }

    @GetMapping
    @Operation(summary = "Obtener capacidad del día actual", 
               description = "Devuelve la capacidad de la planta para el día de hoy")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capacidad obtenida correctamente")
    })
    public ResponseEntity<CapacidadPlanta> getCapacidadHoy() {
        return ResponseEntity.ok(capacidadService.getCapacidad(LocalDate.now()));
    }

    @GetMapping("/{fecha}")
    @Operation(summary = "Obtener capacidad por fecha", 
               description = "Devuelve la capacidad de la planta para una fecha específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capacidad obtenida correctamente")
    })
    public ResponseEntity<CapacidadPlanta> getCapacidadPorFecha(
            @Parameter(description = "Fecha en formato yyyy-MM-dd", example = "2025-12-09")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(capacidadService.getCapacidad(fecha));
    }

    @PostMapping("/actualizar")
    @Operation(summary = "Actualizar ocupación", 
               description = "Actualiza la ocupación de la planta para una fecha específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ocupación actualizada correctamente")
    })
    public ResponseEntity<CapacidadPlanta> actualizarOcupacion(
            @Parameter(description = "Fecha en formato yyyy-MM-dd", example = "2025-12-09")
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @Parameter(description = "Cantidad de ocupación a añadir", example = "50.0")
            @RequestParam("ocupacion") float ocupacion) {
        capacidadService.actualizarCapacidad(fecha, ocupacion);
        return ResponseEntity.ok(capacidadService.getCapacidad(fecha));
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping", description = "Verifica que el servidor está activo")
    @ApiResponse(responseCode = "200", description = "Servidor activo")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("PONG");
    }
}
