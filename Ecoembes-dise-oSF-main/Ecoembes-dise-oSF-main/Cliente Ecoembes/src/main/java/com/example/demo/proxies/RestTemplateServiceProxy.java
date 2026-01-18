package com.example.demo.proxies;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;
import com.example.demo.data.Credentials;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación del proxy de servicios usando Spring RestTemplate.
 * Alternativa a HttpServiceProxy para entornos Spring.
 * Patrón Singleton para garantizar una única instancia.
 */
public class RestTemplateServiceProxy implements IEcoembesServiceProxy {

    private static RestTemplateServiceProxy instance;
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private String token;

    private RestTemplateServiceProxy() {
        this.restTemplate = new RestTemplate();
        this.baseUrl = "http://localhost:9000/api";
    }

    /**
     * Constructor con URL personalizada (para testing).
     * @param baseUrl URL base del servidor
     */
    private RestTemplateServiceProxy(String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    /**
     * Obtiene la instancia única del proxy (Singleton).
     * @return Instancia del RestTemplateServiceProxy
     */
    public static synchronized RestTemplateServiceProxy getInstance() {
        if (instance == null) {
            instance = new RestTemplateServiceProxy();
        }
        return instance;
    }

    /**
     * Obtiene una instancia con URL personalizada (para testing).
     * @param baseUrl URL base del servidor
     * @return Nueva instancia del RestTemplateServiceProxy
     */
    public static RestTemplateServiceProxy getInstance(String baseUrl) {
        return new RestTemplateServiceProxy(baseUrl);
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    /**
     * Crea los headers HTTP con el token de autorización.
     * @return HttpHeaders configurados
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    @Override
    public String login(Credentials credentials) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Credentials> request = new HttpEntity<>(credentials, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/auth/login",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error en Login: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión en login: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout(String token) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/auth/logout?token=" + token,
                    HttpMethod.POST,
                    null,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Error en Logout: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión en logout: " + e.getMessage(), e);
        }
    }

    @Override
    public Contenedor createContenedor(Contenedor contenedor) {
        try {
            HttpEntity<Contenedor> request = new HttpEntity<>(contenedor, createAuthHeaders());

            ResponseEntity<Contenedor> response = restTemplate.exchange(
                    baseUrl + "/contenedores",
                    HttpMethod.POST,
                    request,
                    Contenedor.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al crear contenedor: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión creando contenedor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Contenedor> getContenedoresByUbicacion(String ubicacion) {
        try {
            HttpEntity<?> request = new HttpEntity<>(createAuthHeaders());

            ResponseEntity<List<Contenedor>> response = restTemplate.exchange(
                    baseUrl + "/contenedores/ubicacion/" + ubicacion,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Contenedor>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al consultar contenedores por ubicación: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión consultando contenedores: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Contenedor> getContenedoresByNivelYFecha(Contenedor.NivelLlenado nivel,
                                                          LocalDateTime inicio,
                                                          LocalDateTime fin) {
        try {
            String url = String.format("%s/contenedores/nivel-fecha?nivel=%s&inicio=%s&fin=%s",
                    baseUrl,
                    nivel.name(),
                    inicio.format(DateTimeFormatter.ISO_DATE_TIME),
                    fin.format(DateTimeFormatter.ISO_DATE_TIME));

            HttpEntity<?> request = new HttpEntity<>(createAuthHeaders());

            ResponseEntity<List<Contenedor>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<Contenedor>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al consultar contenedores por nivel y fecha: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión consultando contenedores: " + e.getMessage(), e);
        }
    }

    @Override
    public Asignacion assignContenedoresToPlanta(Asignacion asignacion) {
        try {
            HttpEntity<Asignacion> request = new HttpEntity<>(asignacion, createAuthHeaders());

            ResponseEntity<Asignacion> response = restTemplate.exchange(
                    baseUrl + "/asignaciones",
                    HttpMethod.POST,
                    request,
                    Asignacion.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al asignar contenedores: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión asignando contenedores: " + e.getMessage(), e);
        }
    }
}
