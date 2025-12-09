package com.example.demo.service;

import com.example.demo.model.CapacidadPlanta;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CapacidadService {

    private final Map<LocalDate, CapacidadPlanta> capacidades = new HashMap<>();

    public CapacidadService() {
        // Initialize with default capacity
        LocalDate today = LocalDate.now();
        capacidades.put(today, new CapacidadPlanta(today, 800.0f, 600.0f, 200.0f));
    }

    public CapacidadPlanta getCapacidad(LocalDate fecha) {
        return capacidades.computeIfAbsent(fecha,
                f -> new CapacidadPlanta(f, 800.0f, 600.0f, 200.0f));
    }

    public void actualizarCapacidad(LocalDate fecha, float ocupacion) {
        CapacidadPlanta cap = getCapacidad(fecha);
        cap.setOcupacionActual(cap.getOcupacionActual() + ocupacion);
        cap.setCapacidadDisponible(cap.getCapacidadTotal() - cap.getOcupacionActual());
    }
}
