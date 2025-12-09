package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Personal;

@Service
public class LoginService {

    private final Map<String, String> passwords = new HashMap<>();
    private final Map<String, String> activeTokens = new HashMap<>(); // token → email
    private final PersonalService personalService;

    public LoginService(PersonalService personalService) {
        this.personalService = personalService;
        
        // Usuario administrador por defecto
        passwords.put("admin@ecoembes.com", "admin");
    }

    /**
     * Login con generación de token basado en timestamp
     * Requisito: "se utilizará el timestamp del momento en que se realiza el login"
     */
    public Optional<String> login(String email, String password) {
        if (!passwords.containsKey(email)) return Optional.empty();
        if (!passwords.get(email).equals(password)) return Optional.empty();

        // Generar token usando timestamp (en milisegundos)
        String token = String.valueOf(System.currentTimeMillis());
        activeTokens.put(token, email);
        
        // Actualizar el token en la entidad Personal
        personalService.getPersonalByEmail(email).ifPresent(personal -> {
            personal.setToken(Long.parseLong(token));
            personalService.updatePersonal(email, personal);
        });
        
        return Optional.of(token);
    }

    /**
     * Logout - Elimina el token y la información asociada
     * Requisito: "eliminar el token (y el resto de información asociada)"
     */
    public Optional<String> logout(String email) {
        // Buscar y eliminar el token asociado al email
        String token = activeTokens.entrySet().stream()
                .filter(e -> e.getValue().equals(email))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (token != null) {
            activeTokens.remove(token);
            
            // Limpiar token en la base de datos
            personalService.getPersonalByEmail(email).ifPresent(personal -> {
                personal.setToken(null);
                personalService.updatePersonal(email, personal);
            });
            
            return Optional.of("Logout exitoso");
        }

        return Optional.empty();
    }

    /**
     * Logout por token - Elimina el token y la información asociada
     */
    public Optional<String> logoutByToken(String token) {
        String email = activeTokens.get(token);
        
        if (email != null) {
            activeTokens.remove(token);
            
            // Limpiar token en la base de datos
            personalService.getPersonalByEmail(email).ifPresent(personal -> {
                personal.setToken(null);
                personalService.updatePersonal(email, personal);
            });
            
            return Optional.of("Logout exitoso");
        }

        return Optional.empty();
    }

    public Optional<Personal> register(String email, String password) {
        if (passwords.containsKey(email)) {
            return Optional.empty(); // Ya existe
        }

        passwords.put(email, password);

        // Crear nuevo empleado
        Personal nuevo = new Personal();
        nuevo.setEmail(email);
        nuevo.setNombre("Usuario Nuevo");
        nuevo.setFechaAlta(java.time.LocalDate.now());

        Personal creado = personalService.createPersonal(nuevo);
        return Optional.ofNullable(creado);
    }

    /**
     * Valida si un token está activo
     * Requisito: "mantiene en memoria la lista de tokens activos"
     */
    public boolean isTokenValid(String token) {
        return activeTokens.containsKey(token);
    }

    public Optional<String> getEmailByToken(String token) {
        return Optional.ofNullable(activeTokens.get(token));
    }

    /**
     * Obtiene el mapa de tokens activos (para SessionStateManager)
     */
    public Map<String, String> getActiveTokens() {
        return new HashMap<>(activeTokens);
    }
}
