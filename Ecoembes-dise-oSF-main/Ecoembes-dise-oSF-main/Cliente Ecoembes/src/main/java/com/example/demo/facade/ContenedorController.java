package com.example.demo.facade;

import com.example.demo.ServiceProxy.ServiceProxy;
import com.example.demo.dto.ContenedorDTO;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Controlador para la gestión de contenedores de reciclaje.
 */
public class ContenedorController {

    private final ServiceProxy serviceProxy;

    public ContenedorController() {
        this.serviceProxy = ServiceProxy.getInstance();
    }

    public ContenedorDTO crearContenedor(ContenedorDTO dto) throws Exception {
        return serviceProxy.createContenedor(dto);
    }

    public List<ContenedorDTO> buscarPorUbicacion(String ubicacion) throws Exception {
        return serviceProxy.getContenedoresByUbicacion(ubicacion);
    }

    public List<ContenedorDTO> buscarPorNivelYFecha(ContenedorDTO.NivelLlenado nivel, LocalDateTime inicio,
            LocalDateTime fin) throws Exception {
        return serviceProxy.getContenedoresByNivelYFecha(nivel, inicio, fin);
    }
}
