package com.example.demo.service;

import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.factory.PlantaGatewayFactory;
import com.example.demo.repository.PlantaReciclajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlantaReciclajeService {

    private final PlantaReciclajeRepository plantaRepository;
    private final PlantaGatewayFactory plantaGatewayFactory;

    public PlantaReciclajeService(PlantaReciclajeRepository plantaRepository,
            PlantaGatewayFactory plantaGatewayFactory) {
        this.plantaRepository = plantaRepository;
        this.plantaGatewayFactory = plantaGatewayFactory;
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

        if (cambios.getNombre() != null)
            planta.setNombre(cambios.getNombre());
        if (cambios.getUbicacion() != null)
            planta.setUbicacion(cambios.getUbicacion());
        if (cambios.getCapacidadDiaria() > 0)
            planta.setCapacidadDiaria(cambios.getCapacidadDiaria());
        if (planta.isActiva() != cambios.isActiva()) {
            planta.setActiva(cambios.isActiva());
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

    public com.example.demo.dto.CapacidadPlantaDTO getCapacidadReal(Long plantaId, java.time.LocalDate fecha) {
        Optional<PlantaReciclaje> plantaOpt = plantaRepository.findById(plantaId);

        if (plantaOpt.isEmpty()) {
            return null;
        }

        PlantaReciclaje planta = plantaOpt.get();

        try {
            // El Service crea el Gateway directamente según el tipo de planta
            var gateway = crearGateway(planta.getNombre());
            return gateway.consultarCapacidadDisponible(fecha);
        } catch (Exception e) {
            // En caso de error, devolvemos un objeto vacío o null
            return new com.example.demo.dto.CapacidadPlantaDTO(fecha, 0, 0, 0);
        }
    }

    /**
     * Crea el Gateway apropiado según el nombre de la planta
     * El Service determina el tipo y usa el Factory para construir
     */
    private com.example.demo.gateway.IPlantaReciclajeGateway crearGateway(String nombrePlanta) {
        if (nombrePlanta == null) {
            throw new IllegalArgumentException("El nombre de la planta no puede ser nulo");
        }

        String nombreLower = nombrePlanta.toLowerCase();

        // El Service decide el tipo (lógica de negocio)
        com.example.demo.factory.PlantaGatewayFactory.TipoPlanta tipo;
        if (nombreLower.contains("plassb")) {
            tipo = com.example.demo.factory.PlantaGatewayFactory.TipoPlanta.PLASSB;
        } else if (nombreLower.contains("contsocket")) {
            tipo = com.example.demo.factory.PlantaGatewayFactory.TipoPlanta.CONTSOCKET;
        } else {
            throw new IllegalArgumentException("Tipo de planta desconocido: " + nombrePlanta);
        }

        // El Factory construye usando su configuración inyectada
        return plantaGatewayFactory.crearGateway(tipo, nombrePlanta);
    }
}
