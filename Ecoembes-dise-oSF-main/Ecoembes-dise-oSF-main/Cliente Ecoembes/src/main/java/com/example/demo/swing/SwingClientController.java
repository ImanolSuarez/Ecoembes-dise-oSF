package com.example.demo.swing;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;
import com.example.demo.data.Credentials;
import com.example.demo.proxies.HttpServiceProxy;
import com.example.demo.proxies.IEcoembesServiceProxy;

/**
 * Controlador para la interfaz Swing del cliente Ecoembes.
 * Actúa como intermediario entre la GUI y el proxy de servicios.
 * Implementa el patrón Controller de MVC.
 */
public class SwingClientController {

    private final IEcoembesServiceProxy serviceProxy;
    private String currentToken;
    private String currentUserEmail;

    /**
     * Constructor por defecto que usa HttpServiceProxy.
     */
    public SwingClientController() {
        this.serviceProxy = HttpServiceProxy.getInstance();
    }

    /**
     * Constructor que permite inyectar una implementación específica del proxy.
     * Útil para testing o para usar RestTemplateServiceProxy.
     * @param serviceProxy Implementación del proxy a utilizar
     */
    public SwingClientController(IEcoembesServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    // ==================== AUTENTICACIÓN ====================

    /**
     * Realiza el login del usuario.
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Token de sesión si el login es exitoso
     * @throws RuntimeException si las credenciales son inválidas
     */
    public String login(String email, String password) {
        Credentials credentials = new Credentials(email, password);
        String token = serviceProxy.login(credentials);
        
        if (token != null) {
            this.currentToken = token;
            this.currentUserEmail = email;
            serviceProxy.setToken(token);
        }
        
        return token;
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        if (currentToken != null) {
            serviceProxy.logout(currentToken);
            currentToken = null;
            currentUserEmail = null;
        }
    }

    /**
     * Verifica si hay una sesión activa.
     * @return true si hay un usuario autenticado
     */
    public boolean isAuthenticated() {
        return currentToken != null;
    }

    /**
     * Obtiene el email del usuario actual.
     * @return Email del usuario autenticado o null
     */
    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    /**
     * Obtiene el token de sesión actual.
     * @return Token de sesión o null
     */
    public String getCurrentToken() {
        return currentToken;
    }

    // ==================== CONTENEDORES ====================

    /**
     * Crea un nuevo contenedor.
     * @param contenedor Datos del contenedor
     * @return Contenedor creado
     */
    public Contenedor crearContenedor(Contenedor contenedor) {
        return serviceProxy.createContenedor(contenedor);
    }

    /**
     * Busca contenedores por ubicación.
     * @param ubicacion Nombre de la ubicación
     * @return Lista de contenedores
     */
    public List<Contenedor> buscarContenedoresPorUbicacion(String ubicacion) {
        return serviceProxy.getContenedoresByUbicacion(ubicacion);
    }

    /**
     * Busca contenedores por nivel de llenado y rango de fechas.
     * @param nivel Nivel de llenado
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de contenedores
     */
    public List<Contenedor> buscarContenedoresPorNivelYFecha(
            Contenedor.NivelLlenado nivel,
            LocalDateTime inicio,
            LocalDateTime fin) {
        return serviceProxy.getContenedoresByNivelYFecha(nivel, inicio, fin);
    }

    // ==================== ASIGNACIONES ====================

    /**
     * Asigna contenedores a una planta de reciclaje.
     * @param asignacion Datos de la asignación
     * @return Asignación creada
     */
    public Asignacion asignarContenedoresAPlanta(Asignacion asignacion) {
        return serviceProxy.assignContenedoresToPlanta(asignacion);
    }

    /**
     * Obtiene el proxy de servicios actual.
     * Útil para operaciones avanzadas o debugging.
     * @return Instancia del proxy de servicios
     */
    public IEcoembesServiceProxy getServiceProxy() {
        return serviceProxy;
    }
}
