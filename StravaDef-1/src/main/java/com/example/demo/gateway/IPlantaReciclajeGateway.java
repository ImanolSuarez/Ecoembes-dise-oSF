package com.example.demo.gateway;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.dto.PlantaReciclajeDTO;
import com.example.demo.dto.RecepcionDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Gateway Interface - Patrón Service Gateway
 * Define el contrato para comunicarse con servicios externos de plantas de
 * reciclaje
 * Implementaciones: PlasSBGateway (REST) y ContSocketGateway (Sockets)
 */
public interface IPlantaReciclajeGateway {

    /**
     * Obtiene información básica de la planta
     * 
     * @return Información de la planta en formato DTO
     */
    PlantaReciclajeDTO obtenerInformacionPlanta();

    /**
     * Consulta la capacidad disponible de la planta para una fecha
     * 
     * @param fecha Fecha para consultar capacidad
     * @return Capacidad disponible en toneladas
     */
    CapacidadPlantaDTO consultarCapacidadDisponible(LocalDate fecha);

    /**
     * Notifica a la planta sobre una nueva asignación de contenedores
     * 
     * @param asignacion Datos de la asignación
     * @return true si la notificación fue exitosa
     */
    boolean notificarAsignacion(AsignacionDTO asignacion);

    /**
     * Registra la recepción de contenedores en la planta
     * 
     * @param recepcion Datos de la recepción
     * @return Confirmación de la recepción
     */
    RecepcionDTO registrarRecepcion(RecepcionDTO recepcion);

    /**
     * Obtiene el historial de recepciones de la planta
     * 
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin    Fecha de fin del rango
     * @return Lista de recepciones
     */
    List<RecepcionDTO> obtenerHistorialRecepciones(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Verifica si el servicio externo está disponible
     * 
     * @return true si el servicio responde
     */
    boolean verificarDisponibilidad();

    /**
     * Obtiene el nombre/identificador de la planta
     * 
     * @return Nombre de la planta
     */
    String getNombrePlanta();

    /**
     * Obtiene el tipo de comunicación (REST o SOCKET)
     * 
     * @return Tipo de comunicación
     */
    String getTipoComunicacion();
}