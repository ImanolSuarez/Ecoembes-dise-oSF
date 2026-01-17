package com.example.demo.ServiceProxy;

import com.example.demo.dto.*;
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

public class ServiceProxy {

    private static ServiceProxy instance;
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl = "http://localhost:9000/api";
    private String token;

    private ServiceProxy() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static synchronized ServiceProxy getInstance() {
        if (instance == null) {
            instance = new ServiceProxy();
        }
        return instance;
    }

    public String login(String email, String password) throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);

        String json = mapper.writeValueAsString(loginDTO);

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
    }

    public void logout(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/logout?token=" + token))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en Logout: " + response.statusCode());
        }
    }

    public ContenedorDTO createContenedor(ContenedorDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/contenedores"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return mapper.readValue(response.body(), ContenedorDTO.class);
        } else {
            throw new RuntimeException("Error al crear contenedor: " + response.statusCode());
        }
    }

    public List<ContenedorDTO> getContenedoresByUbicacion(String ubicacion) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/contenedores/ubicacion/" + ubicacion))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<ContenedorDTO>>() {
            });
        } else {
            throw new RuntimeException("Error al consultar contenedores por ubicación: " + response.statusCode());
        }
    }

    public List<ContenedorDTO> getContenedoresByNivelYFecha(ContenedorDTO.NivelLlenado nivel, LocalDateTime inicio,
            LocalDateTime fin) throws Exception {
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
            return mapper.readValue(response.body(), new TypeReference<List<ContenedorDTO>>() {
            });
        } else {
            throw new RuntimeException("Error al consultar contenedores por fecha: " + response.statusCode());
        }
    }

    public AsignacionDTO assignContenedoresToPlanta(AsignacionDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/asignaciones"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return mapper.readValue(response.body(), AsignacionDTO.class);
        } else {
            throw new RuntimeException("Error al asignar contenedores: " + response.statusCode());
        }
    }
}
