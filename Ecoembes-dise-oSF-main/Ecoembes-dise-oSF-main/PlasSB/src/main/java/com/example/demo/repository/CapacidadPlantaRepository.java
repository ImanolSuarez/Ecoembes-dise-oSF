package com.example.demo.repository;

import com.example.demo.entity.CapacidadPlanta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface CapacidadPlantaRepository extends JpaRepository<CapacidadPlanta, Long> {
    Optional<CapacidadPlanta> findByFecha(LocalDate fecha);
}
