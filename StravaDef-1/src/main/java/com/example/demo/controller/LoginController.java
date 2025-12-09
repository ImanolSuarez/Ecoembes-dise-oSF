package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.facade.EcoembesFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Operaciones de autenticación: login, logout y registro")
public class LoginController {

    private final EcoembesFacade ecoembesFacade;

    public LoginController(EcoembesFacade ecoembesFacade) {
        this.ecoembesFacade = ecoembesFacade;
    }

    @Operation(summary = "Login al sistema", description = "Permite a un usuario iniciar sesión. Retorna un token único y aleatorio.", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Login exitoso, retorna un token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(name = "loginDTO", description = "Credenciales de login del usuario", required = true) @Valid @RequestBody LoginDTO loginDTO) {

        Optional<String> token = ecoembesFacade.login(loginDTO.getEmail(), loginDTO.getPassword());

        if (token.isPresent()) {
            return new ResponseEntity<>(token.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Logout del sistema", description = "Cierra sesión y elimina el token del usuario.", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Logout exitoso"),
            @ApiResponse(responseCode = "404", description = "Not Found: Usuario no encontrado")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Parameter(name = "logoutDTO", description = "Email del usuario para logout", required = true) @Valid @RequestBody LoginDTO logoutDTO) {

        Optional<String> result = ecoembesFacade.logout(logoutDTO.getEmail());

        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Permite registrar un nuevo empleado en el sistema con email y contraseña.", responses = {
            @ApiResponse(responseCode = "201", description = "Created: Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "409", description = "Conflict: El email ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(name = "loginDTO", description = "Email y contraseña para el nuevo usuario", required = true) @Valid @RequestBody LoginDTO loginDTO) {

        Optional<com.example.demo.entity.Personal> personal = ecoembesFacade.register(
                loginDTO.getEmail(),
                loginDTO.getPassword());

        if (personal.isPresent()) {
            return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("El email ya está registrado", HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Validar token", description = "Verifica si un token de sesión es válido y activo.", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Token válido"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Token inválido o expirado")
    })
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(
            @Parameter(name = "token", description = "Token a validar", required = true) @RequestParam String token) {

        boolean isValid = ecoembesFacade.validateToken(token);

        if (isValid) {
            return new ResponseEntity<>("Token válido", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token inválido", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Obtener información de sesión", description = "Obtiene información detallada sobre una sesión activa.", responses = {
            @ApiResponse(responseCode = "200", description = "OK: Información de sesión obtenida"),
            @ApiResponse(responseCode = "404", description = "Not Found: Sesión no encontrada")
    })
    @GetMapping("/session-info")
    public ResponseEntity<String> getSessionInfo(
            @Parameter(name = "token", description = "Token de sesión", required = true) @RequestParam String token) {

        Optional<String> sessionInfo = ecoembesFacade.getSessionInfo(token);

        return sessionInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
