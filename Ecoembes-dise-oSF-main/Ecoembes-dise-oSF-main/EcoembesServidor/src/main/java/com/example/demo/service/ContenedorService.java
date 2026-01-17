package com.example.demo.service;

import com.example.demo.entity.Contenedor;
import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.repository.ContenedorRepository;
import com.example.demo.repository.PlantaReciclajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final PlantaReciclajeRepository plantaRepository;

    public ContenedorService(ContenedorRepository contenedorRepository, 
                            PlantaReciclajeRepository plantaRepository) {
        this.contenedorRepository = contenedorRepository;
        this.plantaRepository = plantaRepository;
    }

    // Obtener todos los contenedores
    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    // Buscar contenedor por ID
    public Optional<Contenedor> findById(String id) {
        return contenedorRepository.findById(id);
    }

    // Crear contenedor
    public Contenedor create(Contenedor contenedor) {
    if (contenedorRepository.existsById(contenedor.getId())) {
        throw new IllegalArgumentException("Ya existe un contenedor con ID: " + contenedor.getId());
    }

    // Inicializa algunos campos si son nulos o cero
    if (contenedor.getNivelLlenado() == null) {
        contenedor.setNivelLlenado(Contenedor.NivelLlenado.VERDE);
    }

    if (contenedor.getNumEnvases() < 0) {
        contenedor.setNumEnvases(0);
    }

    if (contenedor.getUltimaActualizacion() == null) {
        contenedor.setUltimaActualizacion(java.time.LocalDateTime.now());
    }

    return contenedorRepository.save(contenedor);
}

    // Actualizar contenedor
    public Optional<Contenedor> update(String id, Contenedor cambios) {
    Optional<Contenedor> contenedorOpt = contenedorRepository.findById(id);

    if (contenedorOpt.isEmpty()) {
        return Optional.empty();
    }

    Contenedor contenedor = contenedorOpt.get();

    if (cambios.getUbicacion() != null) {
        contenedor.setUbicacion(cambios.getUbicacion());
    }

    if (cambios.getCapacidadTotal() > 0) {
        contenedor.setCapacidadTotal(cambios.getCapacidadTotal());
    }

    if (cambios.getNivelLlenado() != null) {
        contenedor.setNivelLlenado(cambios.getNivelLlenado());
    }

    if (cambios.getPlantaAsignada() != null) {
        contenedor.setPlantaAsignada(cambios.getPlantaAsignada());
    }

    return Optional.of(contenedorRepository.save(contenedor));
}


    // Eliminar contenedor
    public boolean delete(String id) {
        if (!contenedorRepository.existsById(id)) {
            return false;
        }
        contenedorRepository.deleteById(id);
        return true;
    }

    // Buscar por ubicación
    public List<Contenedor> findByUbicacion(String ubicacion) {
        return contenedorRepository.findByUbicacionContaining(ubicacion);
    }

   public List<Contenedor> findByNivelLlenado(Contenedor.NivelLlenado nivel) {
    return contenedorRepository.findByNivelLlenado(nivel);
    }


    // Asignar contenedor a una planta
    public Optional<Contenedor> asignarAPlanta(String contenedorId, Long plantaId) {
        Optional<Contenedor> contenedorOpt = contenedorRepository.findById(contenedorId);
        Optional<PlantaReciclaje> plantaOpt = plantaRepository.findById(plantaId);
        
        if (contenedorOpt.isEmpty() || plantaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Contenedor contenedor = contenedorOpt.get();
        PlantaReciclaje planta = plantaOpt.get();
        
        contenedor.setPlantaAsignada(planta);
        return Optional.of(contenedorRepository.save(contenedor));
    }

    // Desasignar contenedor de planta
    public Optional<Contenedor> desasignarDePlanta(String contenedorId) {
        Optional<Contenedor> contenedorOpt = contenedorRepository.findById(contenedorId);
        
        if (contenedorOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Contenedor contenedor = contenedorOpt.get();
        contenedor.setPlantaAsignada(null);
        return Optional.of(contenedorRepository.save(contenedor));
    }
    
    public List<Contenedor> findByNivelYUbicacion(Contenedor.NivelLlenado nivel, String ubicacion) {
        return contenedorRepository.findByNivelLlenadoAndUbicacionContaining(nivel, ubicacion);
    }

    public List<Contenedor> findByNivelYFecha(Contenedor.NivelLlenado nivel, LocalDateTime inicio, LocalDateTime fin) {
        return contenedorRepository.findByNivelLlenadoAndUltimaActualizacionBetween(nivel, inicio, fin);
    }

    public List<Contenedor> findByNivelUbicacionYFecha(Contenedor.NivelLlenado nivel, String ubicacion,
                                                       LocalDateTime inicio, LocalDateTime fin) {
        return contenedorRepository.findByNivelLlenadoAndUbicacionContainingAndUltimaActualizacionBetween(
                nivel, ubicacion, inicio, fin);
    }

    // Obtener contenedores de una planta
    public List<Contenedor> findByPlanta(Long plantaId) {
        return contenedorRepository.findByPlantaAsignadaId(plantaId);
    }
}
