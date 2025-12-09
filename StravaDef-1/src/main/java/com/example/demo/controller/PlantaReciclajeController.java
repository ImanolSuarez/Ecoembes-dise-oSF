package com.example.demo.controller;

import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.dto.PlantaReciclajeDTO;
import com.example.demo.facade.EcoembesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plantas")
@Tag(name = "Planta Controller", description = "Gestión de plantas de reciclaje")
public class PlantaReciclajeController {

    private final EcoembesFacade ecoembesFacade;

    public PlantaReciclajeController(EcoembesFacade ecoembesFacade) {
        this.ecoembesFacade = ecoembesFacade;
    }

    @Operation(
            summary = "Obtener todas las plantas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida"),
                    @ApiResponse(responseCode = "204", description = "No hay plantas")
            }
    )
    @GetMapping
    public ResponseEntity<List<PlantaReciclajeDTO>> getAllPlantas() {
        List<PlantaReciclaje> plantas = ecoembesFacade.getAllPlantas();
        List<PlantaReciclajeDTO> dtos = plantas.stream()
                .map(this::toDTO)
                .toList();
        return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Obtener planta por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planta encontrada"),
                    @ApiResponse(responseCode = "404", description = "Planta no encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PlantaReciclajeDTO> getPlantaById(@PathVariable Long id) {
        Optional<PlantaReciclaje> planta = ecoembesFacade.getPlantaById(id);
        return planta.map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Crear nueva planta",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Planta creada")
            }
    )
    @PostMapping
    public ResponseEntity<PlantaReciclajeDTO> createPlanta(@RequestBody PlantaReciclajeDTO dto) {
        PlantaReciclaje planta = toEntity(dto);
        PlantaReciclaje creada = ecoembesFacade.createPlanta(planta);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creada));
    }

    @Operation(
            summary = "Actualizar planta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planta actualizada"),
                    @ApiResponse(responseCode = "404", description = "Planta no encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PlantaReciclajeDTO> updatePlanta(@PathVariable Long id, 
                                                            @RequestBody PlantaReciclajeDTO dto) {
        PlantaReciclaje planta = toEntity(dto);
        Optional<PlantaReciclaje> actualizada = ecoembesFacade.updatePlanta(id, planta);
        return actualizada.map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Eliminar planta",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Planta eliminada"),
                    @ApiResponse(responseCode = "404", description = "Planta no encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanta(@PathVariable Long id) {
        boolean deleted = ecoembesFacade.deletePlanta(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private PlantaReciclajeDTO toDTO(PlantaReciclaje p) {
    return new PlantaReciclajeDTO(
            p.getId(),
            p.getNombre(),
            p.getUbicacion(),
            p.getCapacidadDiaria(),
            p.isActiva()
    );
}


    private PlantaReciclaje toEntity(PlantaReciclajeDTO dto) {
        PlantaReciclaje planta = new PlantaReciclaje();
        if (dto.getId() != null) planta.setId(dto.getId());
        planta.setNombre(dto.getNombre());
        planta.setUbicacion(dto.getUbicacion());
        planta.setCapacidadDiaria(dto.getCapacidadDiaria());
        planta.setActiva(dto.isActiva());
        return planta;
    }
}
