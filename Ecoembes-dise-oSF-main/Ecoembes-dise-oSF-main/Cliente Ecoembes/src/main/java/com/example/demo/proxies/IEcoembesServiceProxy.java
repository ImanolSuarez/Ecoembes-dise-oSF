package com.example.demo.proxies;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.data.Asignacion;
import com.example.demo.data.Contenedor;
import com.example.demo.data.Credentials;

/**
 * Interfaz que define el contrato para la comunicación con el servidor Ecoembes.
 * Permite diferentes implementaciones (HttpClient, RestTemplate, etc.).
 */
public interface IEcoembesServiceProxy {

    String login(Credentials credentials);

    void logout(String token);

    void setToken(String token);

    String getToken();

    Contenedor createContenedor(Contenedor contenedor);

    List<Contenedor> getContenedoresByUbicacion(String ubicacion);

    List<Contenedor> getContenedoresByNivelYFecha(Contenedor.NivelLlenado nivel, 
                                                   LocalDateTime inicio, 
                                                   LocalDateTime fin);

    Asignacion assignContenedoresToPlanta(Asignacion asignacion);
}
