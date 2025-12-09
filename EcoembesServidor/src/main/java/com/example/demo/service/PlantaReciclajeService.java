package com.example.demo.service;

import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.repository.PlantaReciclajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlantaReciclajeService {

    private final PlantaReciclajeRepository plantaRepository;

    public PlantaReciclajeService(PlantaReciclajeRepository plantaRepository) {
        this.plantaRepository = plantaRepository;
    }

    public List<PlantaReciclaje> findAll() {
        return plantaRepository.findAll();
    }

    public Optional<PlantaReciclaje> findById(Long id) {
        return plantaRepository.findById(id);
    }

    public PlantaReciclaje create(PlantaReciclaje planta) {
        return plantaRepository.save(planta);
    }

    public Optional<PlantaReciclaje> update(Long id, PlantaReciclaje cambios) {
        Optional<PlantaReciclaje> plantaOpt = plantaRepository.findById(id);
        
        if (plantaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        PlantaReciclaje planta = plantaOpt.get();
        
        if (cambios.getNombre() != null) planta.setNombre(cambios.getNombre());
        if (cambios.getUbicacion() != null) planta.setUbicacion(cambios.getUbicacion());
        if (cambios.getCapacidadDiaria() > 0) planta.setCapacidadDiaria(cambios.getCapacidadDiaria());
        if (planta.isActiva() != cambios.isActiva()) {planta.setActiva(cambios.isActiva());
        }
        
        return Optional.of(plantaRepository.save(planta));
    }

    public boolean delete(Long id) {
        if (!plantaRepository.existsById(id)) {
            return false;
        }
        plantaRepository.deleteById(id);
        return true;
    }

    public List<PlantaReciclaje> findByUbicacion(String ubicacion) {
        return plantaRepository.findByUbicacionContaining(ubicacion);
    }

    public List<PlantaReciclaje> findActivas() {
        return plantaRepository.findByActivaTrue();
    }
}
