package com.example.demo.controller;

import com.example.demo.entity.Asignacion;
import com.example.demo.dto.AsignacionDTO;
import com.example.demo.facade.EcoembesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asignaciones")
@Tag(name = "Asignacion Controller", description = "Gestión de asignaciones de contenedores a plantas")
public class AsignacionController {

        private final EcoembesFacade ecoembesFacade;

        public AsignacionController(EcoembesFacade ecoembesFacade) {
                this.ecoembesFacade = ecoembesFacade;
        }

        @Operation(summary = "Obtener todas las asignaciones", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida"),
                        @ApiResponse(responseCode = "204", description = "No hay asignaciones")
        })
        @GetMapping
        public ResponseEntity<List<AsignacionDTO>> getAllAsignaciones() {
                List<Asignacion> asignaciones = ecoembesFacade.getAllAsignaciones();
                List<AsignacionDTO> dtos = asignaciones.stream()
                                .map(this::toDTO)
                                .toList();
                return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
        }

        @Operation(summary = "Crear nueva asignación", responses = {
                        @ApiResponse(responseCode = "201", description = "Asignación creada"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "404", description = "Planta o empleado no encontrado")
        })
        @PostMapping
        public ResponseEntity<AsignacionDTO> createAsignacion(@RequestBody AsignacionDTO dto) {
                if (dto.getPlantaId() == null || dto.getContenedoresIds() == null ||
                                dto.getContenedoresIds().isEmpty() || dto.getEmpleadoResponsable() == null) {
                        return ResponseEntity.badRequest().build();
                }

                Optional<Asignacion> asignacion = ecoembesFacade.createAsignacion(
                                dto.getPlantaId(),
                                dto.getContenedoresIds(),
                                dto.getFecha() != null ? dto.getFecha() : LocalDate.now(),
                                dto.getEmpleadoResponsable());

                return asignacion.map(a -> ResponseEntity.status(HttpStatus.CREATED).body(toDTO(a)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Eliminar asignación", responses = {
                        @ApiResponse(responseCode = "204", description = "Asignación eliminada"),
                        @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAsignacion(@PathVariable("id") Long id) {
                boolean deleted = ecoembesFacade.deleteAsignacion(id);
                return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Obtener asignaciones por empleado", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida") })
        @GetMapping("/empleado/{email}")
        public ResponseEntity<List<AsignacionDTO>> getByEmpleado(@PathVariable("email") String email) {
                List<Asignacion> asignaciones = ecoembesFacade.getAsignacionesByEmpleado(email);
                List<AsignacionDTO> dtos = asignaciones.stream().map(this::toDTO).toList();
                return ResponseEntity.ok(dtos);
        }

        @Operation(summary = "Obtener asignaciones por planta", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida") })
        @GetMapping("/planta/{plantaId}")
        public ResponseEntity<List<AsignacionDTO>> getByPlanta(@PathVariable("plantaId") Long plantaId) {
                List<Asignacion> asignaciones = ecoembesFacade.getAsignacionesByPlanta(plantaId);
                List<AsignacionDTO> dtos = asignaciones.stream().map(this::toDTO).toList();
                return ResponseEntity.ok(dtos);
        }

        @Operation(summary = "Obtener asignaciones por fecha", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista obtenida") })
        @GetMapping("/fecha/{fecha}")
        public ResponseEntity<List<AsignacionDTO>> getByFecha(
                        @PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
                List<Asignacion> asignaciones = ecoembesFacade.getAsignacionesByFecha(fecha);
                List<AsignacionDTO> dtos = asignaciones.stream().map(this::toDTO).toList();
                return ResponseEntity.ok(dtos);
        }

        private AsignacionDTO toDTO(Asignacion a) {
                List<String> contenedoresIds = a.getContenedoresIds();

                return new AsignacionDTO(
                                a.getId(),
                                a.getPlanta() != null ? a.getPlanta().getId() : null,
                                a.getPlanta() != null ? a.getPlanta().getNombre() : null,
                                contenedoresIds,
                                a.getFecha(),
                                a.getTotalContenedores(),
                                a.getTotalEnvases(),
                                a.getPesoEstimado(),
                                a.getEmpleadoResponsable() != null ? a.getEmpleadoResponsable().getEmail() : null,
                                a.getFechaAsignacion());
        }
}
