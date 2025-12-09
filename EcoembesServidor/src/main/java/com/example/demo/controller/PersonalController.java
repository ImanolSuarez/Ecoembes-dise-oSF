package com.example.demo.controller;

import com.example.demo.dto.PersonalDTO;
import com.example.demo.dto.PersonalDTO.AsignacionSimpleDTO;
import com.example.demo.entity.Personal;
import com.example.demo.facade.EcoembesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personal")
@Tag(name = "Personal Controller", description = "Gestión del personal de Ecoembes")
public class PersonalController {

    private final EcoembesFacade ecoembesFacade;

    public PersonalController(EcoembesFacade ecoembesFacade) {
        this.ecoembesFacade = ecoembesFacade;
    }

    private String normalizeEmail(String email) {
        return email != null ? email.toLowerCase().trim() : null;
    }

    @Operation(
            summary = "Obtener todo el personal",
            description = "Devuelve una lista de todos los empleados registrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Lista obtenida con éxito"),
                    @ApiResponse(responseCode = "204", description = "No Content: No hay personal registrado")
            }
    )
    @GetMapping
    public ResponseEntity<List<PersonalDTO>> getAllPersonal() {
        try {
            List<Personal> personal = ecoembesFacade.getAllPersonal();
            List<PersonalDTO> personalDTO = transformarAPersonalDTOs(personal);
            return personalDTO.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(personalDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Obtener un empleado por email",
            description = "Devuelve la información del empleado identificado por su email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Empleado encontrado"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Empleado no encontrado")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<PersonalDTO> getPersonalByEmail(
            @Parameter(name = "email", description = "Email del empleado", required = true)
            @PathVariable("email") String email) {
        try {
            String normalizedEmail = normalizeEmail(email);
            Optional<Personal> personal = ecoembesFacade.getPersonalByEmail(normalizedEmail);
            
            if (personal.isPresent()) {
                PersonalDTO dto = personalToPersonalDTO(personal.get());
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.notFound().build();
            }        	
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Crear un nuevo empleado",
            description = "Registra un nuevo empleado en el sistema",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created: Empleado creado con éxito"),
                    @ApiResponse(responseCode = "409", description = "Conflict: Ya existe un empleado con ese email"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<PersonalDTO> createPersonal(@RequestBody PersonalDTO personalDTO) {
        try {
            if (personalDTO.getEmail() == null || personalDTO.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (personalDTO.getNombre() == null || personalDTO.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (personalDTO.getFechaAlta() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            personalDTO.setEmail(normalizeEmail(personalDTO.getEmail()));
            
            Personal personalEntity = personalDTOToPersonal(personalDTO);
            Personal createdPersonal = ecoembesFacade.createPersonal(personalEntity);
            
            if (createdPersonal != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(personalToPersonalDTO(createdPersonal));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Actualizar un empleado",
            description = "Actualiza los datos del empleado identificado por su email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Empleado actualizado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Empleado no encontrado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Datos inválidos")
            }
    )
    @PutMapping("/{email}")
    public ResponseEntity<PersonalDTO> updatePersonal(
            @Parameter(name = "email", description = "Email del empleado a actualizar", required = true)
            @PathVariable("email") String email,
            @RequestBody PersonalDTO personalDTO) {
        try {
            if (personalDTO.getNombre() == null || personalDTO.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            String normalizedEmail = normalizeEmail(email);
            personalDTO.setEmail(normalizedEmail);
            
            Personal personalEntity = personalDTOToPersonal(personalDTO);
            Optional<Personal> updatedPersonal = ecoembesFacade.updatePersonal(normalizedEmail, personalEntity);

            return updatedPersonal.map(p -> ResponseEntity.ok(personalToPersonalDTO(p)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Eliminar un empleado",
            description = "Elimina el empleado identificado por su email",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content: Empleado eliminado con éxito"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Empleado no encontrado")
            }
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletePersonal(
            @Parameter(name = "email", description = "Email del empleado a eliminar", required = true)
            @PathVariable("email") String email) {
        try {
            String normalizedEmail = normalizeEmail(email);
            boolean deleted = ecoembesFacade.deletePersonal(normalizedEmail);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== MÉTODOS DE CONVERSIÓN DTO ====================
    
    private PersonalDTO personalToPersonalDTO(Personal personal) {
        PersonalDTO dto = new PersonalDTO(personal.getToken(), personal.getNombre(), personal.getEmail(),
                personal.getFechaAlta());
        
        // Convertir asignaciones a DTOs simples
        if (personal.getAsignacionesRealizadas() != null && !personal.getAsignacionesRealizadas().isEmpty()) {
            List<AsignacionSimpleDTO> asignacionesDTO = personal.getAsignacionesRealizadas().stream()
                .map(a -> new AsignacionSimpleDTO(a.getId(), 
                    a.getPlanta() != null ? a.getPlanta().getId() : null, 
                    a.getFecha(), a.getTotalContenedores()))
                .collect(Collectors.toList());
            dto.setAsignacionesRealizadas(asignacionesDTO);
        }
        
        return dto;
    }

    private List<PersonalDTO> transformarAPersonalDTOs(List<Personal> personal) {
        return personal.stream()
                       .map(this::personalToPersonalDTO)
                       .toList();
    }

    private Personal personalDTOToPersonal(PersonalDTO dto) {
        return new Personal(dto.getNombre(), dto.getEmail(), dto.getFechaAlta());
    }
}