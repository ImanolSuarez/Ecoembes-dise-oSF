package com.example.demo.facade;

import com.example.demo.ServiceProxy.ServiceProxy;
import com.example.demo.dto.AsignacionDTO;

/**
 * Controlador para la gestión de asignaciones de contenedores a plantas.
 */
public class AsignacionController {

    private final ServiceProxy serviceProxy;

    public AsignacionController() {
        this.serviceProxy = ServiceProxy.getInstance();
    }

    public AsignacionDTO asignarAPlanta(AsignacionDTO dto) throws Exception {
        return serviceProxy.assignContenedoresToPlanta(dto);
    }
}
