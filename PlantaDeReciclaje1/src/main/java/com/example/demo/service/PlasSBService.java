package com.example.demo.service;

import com.example.demo.dto.AsignacionDTO;
import com.example.demo.dto.CapacidadPlantaDTO;
import com.example.demo.entity.Asignacion;
import com.example.demo.entity.CapacidadPlanta;
import com.example.demo.repository.AsignacionRepository;
import com.example.demo.repository.CapacidadPlantaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlasSBService {

    private final CapacidadPlantaRepository capacidadRepository;
    private final AsignacionRepository asignacionRepository;

    public PlasSBService(CapacidadPlantaRepository capacidadRepository, AsignacionRepository asignacionRepository) {
        this.capacidadRepository = capacidadRepository;
        this.asignacionRepository = asignacionRepository;
    }

    public CapacidadPlantaDTO getCapacidad(LocalDate fecha) {
        Optional<CapacidadPlanta> capacidad = capacidadRepository.findByFecha(fecha);

        if (capacidad.isPresent()) {
            CapacidadPlanta c = capacidad.get();
            return new CapacidadPlantaDTO(c.getFecha(), c.getCapacidadTotal(),
                    c.getCapacidadDisponible(), c.getOcupacionActual());
        } else {
            // Create default capacity if not found
            CapacidadPlanta nuevaCapacidad = new CapacidadPlanta(fecha, 1000.0f, 800.0f, 200.0f);
            capacidadRepository.save(nuevaCapacidad);
            return new CapacidadPlantaDTO(fecha, 1000.0f, 800.0f, 200.0f);
        }
    }

    public void notificarAsignacion(AsignacionDTO asignacionDTO) {
        Asignacion asignacion = new Asignacion(
                asignacionDTO.getId().toString(),
                LocalDateTime.now(),
                asignacionDTO.getPesoEstimado());
        asignacionRepository.save(asignacion);
    }

    public String getHealth() {
        return "PlasSB is running";
    }
}
