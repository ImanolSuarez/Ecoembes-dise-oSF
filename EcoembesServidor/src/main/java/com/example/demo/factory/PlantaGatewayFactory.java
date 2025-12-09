package com.example.demo.factory;

import com.example.demo.gateway.IPlantaReciclajeGateway;
import com.example.demo.gateway.impl.ContSocketGateway;
import com.example.demo.gateway.impl.PlasSBGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern - Crea instancias de Service Gateway según el tipo de planta
 * 
 * IMPORTANTE: Esta es la ÚNICA clase que debe tener if/else o switch-case
 * para decidir qué tipo de gateway crear. El resto del código (Services)
 * debe ser agnóstico al tipo concreto de gateway.
 */
@Component
public class PlantaGatewayFactory {

    private static final Logger logger = LoggerFactory.getLogger(PlantaGatewayFactory.class);

    // Configuración de PlasSB desde properties
    @Value("${planta.plassb.host:localhost}")
    private String plassbHost;

    @Value("${planta.plassb.port:8081}")
    private int plassbPort;

    @Value("${planta.plassb.nombre:PlasSB Ltd.}")
    private String plassbNombre;

    // Configuración de ContSocket desde properties
    @Value("${planta.contsocket.host:localhost}")
    private String contsocketHost;

    @Value("${planta.contsocket.port:9090}")
    private int contsocketPort;

    @Value("${planta.contsocket.nombre:ContSocket Ltd.}")
    private String contsocketNombre;

    /**
     * Tipo de planta/comunicación
     */
    public enum TipoPlanta {
        PLASSB,      // Comunicación REST
        CONTSOCKET   // Comunicación Sockets
    }

    /**
     * Crea un gateway según el tipo de planta
     * 
     * @param tipo Tipo de planta (PLASSB o CONTSOCKET)
     * @return Instancia del gateway correspondiente
     */
    public IPlantaReciclajeGateway crearGateway(TipoPlanta tipo) {
        logger.info("Creando gateway para planta tipo: {}", tipo);

        // ÚNICO LUGAR con if/else sobre el tipo de servicio
        switch (tipo) {
            case PLASSB:
                logger.info("Creando PlasSBGateway: {}:{}", plassbHost, plassbPort);
                return new PlasSBGateway(plassbHost, plassbPort, plassbNombre);

            case CONTSOCKET:
                logger.info("Creando ContSocketGateway: {}:{}", contsocketHost, contsocketPort);
                return new ContSocketGateway(contsocketHost, contsocketPort, contsocketNombre);

            default:
                logger.error("Tipo de planta desconocido: {}", tipo);
                throw new IllegalArgumentException("Tipo de planta no soportado: " + tipo);
        }
    }

    /**
     * Crea un gateway por nombre de planta (alternativo)
     * 
     * @param nombrePlanta Nombre de la planta
     * @return Instancia del gateway correspondiente
     */
    public IPlantaReciclajeGateway crearGatewayPorNombre(String nombrePlanta) {
        logger.info("Creando gateway para planta: {}", nombrePlanta);

        if (nombrePlanta == null) {
            throw new IllegalArgumentException("El nombre de la planta no puede ser nulo");
        }

        // ÚNICO LUGAR con if/else sobre el tipo de servicio
        if (nombrePlanta.toLowerCase().contains("plassb")) {
            return crearGateway(TipoPlanta.PLASSB);
        } else if (nombrePlanta.toLowerCase().contains("contsocket")) {
            return crearGateway(TipoPlanta.CONTSOCKET);
        } else {
            logger.warn("Planta desconocida: {}, usando PlasSB por defecto", nombrePlanta);
            return crearGateway(TipoPlanta.PLASSB);
        }
    }

    /**
     * Crea un gateway personalizado con configuración específica
     * 
     * @param tipo Tipo de planta
     * @param host Host del servidor
     * @param port Puerto del servidor
     * @param nombre Nombre de la planta
     * @return Instancia del gateway correspondiente
     */
    public IPlantaReciclajeGateway crearGatewayPersonalizado(
            TipoPlanta tipo, String host, int port, String nombre) {
        
        logger.info("Creando gateway personalizado: {} - {}:{}", nombre, host, port);

        // ÚNICO LUGAR con if/else sobre el tipo de servicio
        switch (tipo) {
            case PLASSB:
                return new PlasSBGateway(host, port, nombre);

            case CONTSOCKET:
                return new ContSocketGateway(host, port, nombre);

            default:
                throw new IllegalArgumentException("Tipo de planta no soportado: " + tipo);
        }
    }

    /**
     * Crea todos los gateways configurados
     * Útil para inicialización o verificación de disponibilidad
     * 
     * @return Lista de todos los gateways
     */
    public java.util.List<IPlantaReciclajeGateway> crearTodosLosGateways() {
        logger.info("Creando todos los gateways configurados");
        
        java.util.List<IPlantaReciclajeGateway> gateways = new java.util.ArrayList<>();
        
        try {
            gateways.add(crearGateway(TipoPlanta.PLASSB));
        } catch (Exception e) {
            logger.error("Error al crear gateway PlasSB: {}", e.getMessage());
        }
        
        try {
            gateways.add(crearGateway(TipoPlanta.CONTSOCKET));
        } catch (Exception e) {
            logger.error("Error al crear gateway ContSocket: {}", e.getMessage());
        }
        
        logger.info("Total de gateways creados: {}", gateways.size());
        return gateways;
    }

    /**
     * Verifica la disponibilidad de todas las plantas configuradas
     * 
     * @return Map con el estado de cada planta
     */
    public java.util.Map<String, Boolean> verificarDisponibilidadPlantas() {
        logger.info("Verificando disponibilidad de todas las plantas");
        
        java.util.Map<String, Boolean> estadoPlantas = new java.util.HashMap<>();
        
        for (IPlantaReciclajeGateway gateway : crearTodosLosGateways()) {
            boolean disponible = gateway.verificarDisponibilidad();
            estadoPlantas.put(gateway.getNombrePlanta(), disponible);
            
            logger.info("Planta {}: {}", 
                       gateway.getNombrePlanta(), 
                       disponible ? "DISPONIBLE" : "NO DISPONIBLE");
        }
        
        return estadoPlantas;
    }

    // Getters para testing
    public String getPlassbHost() { return plassbHost; }
    public int getPlassbPort() { return plassbPort; }
    public String getContsocketHost() { return contsocketHost; }
    public int getContsocketPort() { return contsocketPort; }
}