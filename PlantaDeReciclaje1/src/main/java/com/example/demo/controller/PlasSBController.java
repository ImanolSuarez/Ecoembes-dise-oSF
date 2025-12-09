package com.example.demo.controller;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.service.PlasSBService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "PlasSB Controller", description = "API REST de la planta de reciclaje PlasSB")
public class PlasSBController {

    private final PlasSBService plasSBService;

    public PlasSBController(PlasSBService plasSBService) {
        this.plasSBService = plasSBService;
    }

    @Operation(summary = "Consultar capacidad de la planta", description = "Obtiene la capacidad disponible de la planta para una fecha específica")
    @GetMapping("/capacity")
    public ResponseEntity<CapacidadPlantaDTO> getCapacity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        CapacidadPlantaDTO capacidad = plasSBService.getCapacidad(date);
        return ResponseEntity.ok(capacidad);
    }

    @Operation(summary = "Notificar asignación", description = "Recibe una notificación de asignación de contenedores")
    @PostMapping("/asignacion")
    public ResponseEntity<Void> notificarAsignacion(@RequestBody AsignacionDTO asignacionDTO) {
        plasSBService.notificarAsignacion(asignacionDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Health check", description = "Verifica que el servicio está disponible")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(plasSBService.getHealth());
    }
}
