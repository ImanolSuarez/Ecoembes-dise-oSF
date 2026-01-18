package com.example.demo.console;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;
import com.example.demo.data.Credentials;
import com.example.demo.proxies.HttpServiceProxy;
import com.example.demo.proxies.IEcoembesServiceProxy;

/**
 * ConsoleClient class serves as a basic client implementation for the Ecoembes service
 * in a console environment. This class manages user interactions through the console,
 * performing operations such as user login, loading containers, and creating assignments.
 * It utilizes the IEcoembesServiceProxy to interact with the service layer,
 * enabling the application to execute various container management functionalities.
 */
public class ConsoleClient {

    // Service proxy for interacting with the Ecoembes service using HTTP-based implementation
    private final IEcoembesServiceProxy serviceProxy = HttpServiceProxy.getInstance();

    // Token to be used during the session
    private String token;

    // Default email and password for login
    private String defaultEmail = "admin@ecoembes.es";
    private String defaultPassword = "123";

    private static final Logger logger = Logger.getLogger(ConsoleClient.class.getName());

    public static void main(String[] args) {
        ConsoleClient client = new ConsoleClient();

        if (!client.performLogin()) {
            logger.info("Exiting application due to login failure.");
            return;
        }

        if (!client.loadContenedores()) {
            logger.info("No containers found or error loading containers.");
        }

        // Ejemplo de asignación (comentado por defecto)
        // client.performAssignment();
    }

    /**
     * Performs login with default credentials.
     * @return true if login successful, false otherwise
     */
    public boolean performLogin() {
        try {
            Credentials credentials = new Credentials(defaultEmail, defaultPassword);

            token = serviceProxy.login(credentials);
            serviceProxy.setToken(token);
            logger.info("Login successful. Token: " + token);

            return true;
        } catch (RuntimeException e) {
            logger.severe("Login failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads and displays containers from Bilbao.
     * @return true if containers found, false otherwise
     */
    public boolean loadContenedores() {
        try {
            List<Contenedor> contenedores = serviceProxy.getContenedoresByUbicacion("Bilbao");

            if (contenedores == null || contenedores.isEmpty()) {
                logger.info("No containers found.");
                return false;
            }

            logger.info("Found " + contenedores.size() + " containers:");
            contenedores.forEach(c -> 
                logger.info(String.format("  - ID: %s | Ubicación: %s | Nivel: %s | Envases: %d",
                    c.id(), c.ubicacion(), c.nivelLlenado(), c.numEnvases()))
            );

            return true;
        } catch (RuntimeException e) {
            logger.severe("Failed to load containers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads containers filtered by fill level.
     * @param nivel Fill level to filter by
     * @return true if containers found, false otherwise
     */
    public boolean loadContenedoresByNivel(Contenedor.NivelLlenado nivel) {
        try {
            java.time.LocalDateTime inicio = java.time.LocalDateTime.of(2000, 1, 1, 0, 0);
            java.time.LocalDateTime fin = java.time.LocalDateTime.now();

            List<Contenedor> contenedores = serviceProxy.getContenedoresByNivelYFecha(nivel, inicio, fin);

            if (contenedores == null || contenedores.isEmpty()) {
                logger.info("No containers found with level: " + nivel);
                return false;
            }

            logger.info("Found " + contenedores.size() + " containers with level " + nivel + ":");
            contenedores.forEach(c -> 
                logger.info(String.format("  - ID: %s | Ubicación: %s | Envases: %d",
                    c.id(), c.ubicacion(), c.numEnvases()))
            );

            return true;
        } catch (RuntimeException e) {
            logger.severe("Failed to load containers by level: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a new container.
     * @param id Container ID
     * @param ubicacion Location
     * @param codigoPostal Postal code
     * @param capacidad Capacity
     * @return Created container or null if failed
     */
    public Contenedor createContenedor(String id, String ubicacion, String codigoPostal, float capacidad) {
        try {
            Contenedor nuevo = new Contenedor(id, ubicacion, codigoPostal, capacidad);
            Contenedor creado = serviceProxy.createContenedor(nuevo);

            logger.info("Container created successfully: " + creado.id());
            return creado;
        } catch (RuntimeException e) {
            logger.severe("Failed to create container: " + e.getMessage());
            return null;
        }
    }

    /**
     * Assigns a container to a plant.
     * @param contenedorId Container ID to assign
     * @param plantaId Plant ID
     * @return Created assignment or null if failed
     */
    public Asignacion assignToPlanta(String contenedorId, Long plantaId) {
        try {
            Asignacion asignacion = new Asignacion(
                plantaId,
                Collections.singletonList(contenedorId),
                LocalDate.now(),
                defaultEmail
            );

            Asignacion creada = serviceProxy.assignContenedoresToPlanta(asignacion);
            logger.info("Assignment created successfully. Container " + contenedorId + " assigned to plant " + plantaId);

            return creada;
        } catch (RuntimeException e) {
            logger.severe("Failed to assign container: " + e.getMessage());
            return null;
        }
    }

    /**
     * Performs logout.
     */
    public void performLogout() {
        try {
            if (token != null) {
                serviceProxy.logout(token);
                logger.info("Logout successful.");
                token = null;
            }
        } catch (RuntimeException e) {
            logger.severe("Logout failed: " + e.getMessage());
        }
    }

    /**
     * Example method to demonstrate full workflow.
     */
    public void performAssignment() {
        try {
            List<Contenedor> contenedores = serviceProxy.getContenedoresByUbicacion("Bilbao");

            if (contenedores.isEmpty()) {
                logger.info("No containers available for assignment.");
                return;
            }

            Contenedor first = contenedores.get(0);
            logger.info("Attempting to assign container: " + first.id());

            Asignacion result = assignToPlanta(first.id(), 1L);

            if (result != null) {
                logger.info("Assignment completed. Planta: " + result.plantaNombre());
            }
        } catch (RuntimeException e) {
            logger.severe("Assignment workflow failed: " + e.getMessage());
        }
    }
}
