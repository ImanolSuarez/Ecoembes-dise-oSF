package com.example.demo.controller;

import com.example.demo.entity.Contenedor;
import com.example.demo.dto.ContenedorDTO;
import com.example.demo.facade.EcoembesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contenedores")
@Tag(name = "Contenedor Controller", description = "Gestión de contenedores de reciclaje")
public class ContenedorController {

    private final EcoembesFacade ecoembesFacade;

    public ContenedorController(EcoembesFacade ecoembesFacade) {
        this.ecoembesFacade = ecoembesFacade;
    }

    @Operation(
            summary = "Obtener todos los contenedores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida"),
                    @ApiResponse(responseCode = "204", description = "No hay contenedores")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContenedorDTO>> getAllContenedores() {
        List<Contenedor> contenedores = ecoembesFacade.getAllContenedores();
        List<ContenedorDTO> dtos = contenedores.stream()
                .map(this::toDTO)
                .toList();
        return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Obtener contenedor por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contenedor encontrado"),
                    @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDTO> getContenedorById(@PathVariable String id) {
        Optional<Contenedor> contenedor = ecoembesFacade.getContenedorById(id);
        return contenedor.map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Crear nuevo contenedor",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contenedor creado"),
                    @ApiResponse(responseCode = "409", description = "El contenedor ya existe")
            }
    )
    @PostMapping
    public ResponseEntity<ContenedorDTO> createContenedor(@RequestBody ContenedorDTO dto) {
        try {
            Contenedor contenedor = toEntity(dto);
            Contenedor creado = ecoembesFacade.createContenedor(contenedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(
            summary = "Actualizar contenedor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contenedor actualizado"),
                    @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDTO> updateContenedor(@PathVariable String id, 
                                                           @RequestBody ContenedorDTO dto) {
        Contenedor contenedor = toEntity(dto);
        Optional<Contenedor> actualizado = ecoembesFacade.updateContenedor(id, contenedor);
        return actualizado.map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Eliminar contenedor",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Contenedor eliminado"),
                    @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable String id) {
        boolean deleted = ecoembesFacade.deleteContenedor(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Buscar contenedores por ubicación",
            responses = {@ApiResponse(responseCode = "200", description = "Lista obtenida")}
    )
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<ContenedorDTO>> getByUbicacion(@PathVariable String ubicacion) {
        List<Contenedor> contenedores = ecoembesFacade.getContenedoresByUbicacion(ubicacion);
        List<ContenedorDTO> dtos = contenedores.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Buscar contenedores por estado",
            responses = {@ApiResponse(responseCode = "200", description = "Lista obtenida")}
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ContenedorDTO>> getByEstado(@PathVariable Contenedor.NivelLlenado estado) {
        List<Contenedor> contenedores = ecoembesFacade.getContenedoresByLlenado(estado);
        List<ContenedorDTO> dtos = contenedores.stream().map(this::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Asignar contenedor a una planta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contenedor asignado"),
                    @ApiResponse(responseCode = "404", description = "Contenedor o planta no encontrados")
            }
    )
    @PutMapping("/{id}/asignar/{plantaId}")
    public ResponseEntity<ContenedorDTO> asignarAPlanta(@PathVariable String id, 
                                                         @PathVariable Long plantaId) {
        Optional<Contenedor> asignado = ecoembesFacade.asignarContenedorAPlanta(id, plantaId);
        return asignado.map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

@GetMapping("/nivel-ubicacion")
public ResponseEntity<List<ContenedorDTO>> getByNivelUbicacion(
        @RequestParam(name = "nivel") Contenedor.NivelLlenado nivel,
        @RequestParam(name = "ubicacion") String ubicacion) {
    List<Contenedor> contenedores = ecoembesFacade.getContenedoresByNivelYUbicacion(nivel, ubicacion);
    List<ContenedorDTO> dtos = contenedores.stream().map(this::toDTO).toList();
    return ResponseEntity.ok(dtos);
}

@GetMapping("/nivel-fecha")
@Operation(summary = "Buscar contenedores por nivel y rango de fecha")
public ResponseEntity<List<ContenedorDTO>> getByNivelYFecha(
        @RequestParam(name = "nivel") Contenedor.NivelLlenado nivel,
        @RequestParam(name = "inicio") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam(name = "fin") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

    List<Contenedor> contenedores = ecoembesFacade.getContenedoresByNivelYFecha(nivel, inicio, fin);
    List<ContenedorDTO> dtos = contenedores.stream().map(this::toDTO).toList();
    return ResponseEntity.ok(dtos);
}

@GetMapping("/nivel-ubicacion-fecha")
@Operation(summary = "Buscar contenedores por nivel, ubicación y rango de fecha")
public ResponseEntity<List<ContenedorDTO>> getByNivelUbicacionYFecha(
        @RequestParam(name = "nivel") Contenedor.NivelLlenado nivel,
        @RequestParam(name = "ubicacion") String ubicacion,
        @RequestParam(name = "inicio") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam(name = "fin") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

    List<Contenedor> contenedores = ecoembesFacade.getContenedoresByNivelUbicacionYFecha(
            nivel, ubicacion, inicio, fin);

    List<ContenedorDTO> dtos = contenedores.stream().map(this::toDTO).toList();
    return ResponseEntity.ok(dtos);
}

    // Contenedor -> ContenedorDTO
    private ContenedorDTO toDTO(Contenedor c) {
        return new ContenedorDTO(
                c.getId(),
                c.getUbicacion(),
                c.getCodigoPostal(),
                c.getCapacidadTotal(),
                c.getNivelLlenado(),
                c.getNumEnvases(),
                c.getUltimaActualizacion() != null 
                    ? c.getUltimaActualizacion().atOffset(ZoneOffset.UTC)
                    : null
        );
    }

    // ContenedorDTO -> Contenedor
    private Contenedor toEntity(ContenedorDTO dto) {
        Contenedor contenedor = new Contenedor();
        contenedor.setId(dto.getId());
        contenedor.setUbicacion(dto.getUbicacion());
        contenedor.setCodigoPostal(dto.getCodigoPostal());
        contenedor.setCapacidadTotal(dto.getCapacidadTotal());
        contenedor.setNivelLlenado(dto.getNivelLlenado());
        contenedor.setNumEnvases(dto.getNumEnvases());
        if (dto.getUltimaActualizacion() != null) {
        contenedor.setUltimaActualizacion(dto.getUltimaActualizacion().toLocalDateTime());
    }
        return contenedor;
    }

}
