package com.example.demo.proxies;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;
import com.example.demo.data.Credentials;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación del proxy de servicios usando Java HttpClient.
 * Patrón Singleton para garantizar una única instancia.
 */
public class HttpServiceProxy implements IEcoembesServiceProxy {

    private static HttpServiceProxy instance;
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private String token;

    private HttpServiceProxy() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.baseUrl = "http://localhost:9000/api";
    }

    /**
     * Constructor con URL personalizada (para testing).
     * @param baseUrl URL base del servidor
     */
    private HttpServiceProxy(String baseUrl) {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.baseUrl = baseUrl;
    }

    /**
     * Obtiene la instancia única del proxy (Singleton).
     * @return Instancia del HttpServiceProxy
     */
    public static synchronized HttpServiceProxy getInstance() {
        if (instance == null) {
            instance = new HttpServiceProxy();
        }
        return instance;
    }

    /**
     * Obtiene una instancia con URL personalizada (para testing).
     * @param baseUrl URL base del servidor
     * @return Nueva instancia del HttpServiceProxy
     */
    public static HttpServiceProxy getInstance(String baseUrl) {
        return new HttpServiceProxy(baseUrl);
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String login(Credentials credentials) {
        try {
            String json = mapper.writeValueAsString(credentials);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Error en Login: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión en login: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/logout?token=" + token))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en Logout: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión en logout: " + e.getMessage(), e);
        }
    }

    @Override
    public Contenedor createContenedor(Contenedor contenedor) {
        try {
            String json = mapper.writeValueAsString(contenedor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/contenedores"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return mapper.readValue(response.body(), Contenedor.class);
            } else {
                throw new RuntimeException("Error al crear contenedor: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión creando contenedor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Contenedor> getContenedoresByUbicacion(String ubicacion) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/contenedores/ubicacion/" + ubicacion))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Contenedor>>() {});
            } else {
                throw new RuntimeException("Error al consultar contenedores por ubicación: " + response.statusCode());
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

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Contenedor>>() {});
            } else {
                throw new RuntimeException("Error al consultar contenedores por nivel y fecha: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión consultando contenedores: " + e.getMessage(), e);
        }
    }

    @Override
    public Asignacion assignContenedoresToPlanta(Asignacion asignacion) {
        try {
            String json = mapper.writeValueAsString(asignacion);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/asignaciones"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return mapper.readValue(response.body(), Asignacion.class);
            } else {
                throw new RuntimeException("Error al asignar contenedores: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión asignando contenedores: " + e.getMessage(), e);
        }
    }
}
