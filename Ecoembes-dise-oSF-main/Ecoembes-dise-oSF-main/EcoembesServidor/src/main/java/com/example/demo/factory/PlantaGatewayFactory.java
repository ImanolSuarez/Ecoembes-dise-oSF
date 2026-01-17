package com.example.demo.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.gateway.IPlantaReciclajeGateway;
import com.example.demo.gateway.impl.ContSocketGateway;
import com.example.demo.gateway.impl.PlasSBGateway;

/**
 * FACTORY (Factory Pattern) - Spring Component
 * 
 * Responsabilidad: Construcción de Gateways con configuración centralizada
 * - Es un bean de Spring (@Component)
 * - Lee configuración de application.properties
 * - Construye instancias listas para usar
 */
@Component
public class PlantaGatewayFactory {

    @Value("${planta.plassb.host:localhost}")
    private String plassbHost;

    @Value("${planta.plassb.port:8081}")
    private int plassbPort;

    @Value("${planta.contsocket.host:localhost}")
    private String contsocketHost;

    @Value("${planta.contsocket.port:9090}")
    private int contsocketPort;

    public enum TipoPlanta {
        PLASSB,
        CONTSOCKET
    }

    public IPlantaReciclajeGateway crearGateway(TipoPlanta tipo, String nombre) {
        switch (tipo) {
            case PLASSB:
                return new PlasSBGateway(plassbHost, plassbPort, nombre);
               
            case CONTSOCKET:
                return new ContSocketGateway(contsocketHost, contsocketPort, nombre);
            
            default:
                throw new IllegalArgumentException("Tipo de planta no soportado: " + tipo);
        }
    }
}

