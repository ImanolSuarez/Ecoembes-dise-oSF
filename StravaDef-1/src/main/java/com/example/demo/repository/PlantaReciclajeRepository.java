package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.PlantaReciclaje;

public interface PlantaReciclajeRepository extends JpaRepository<PlantaReciclaje, Long> {
    List<PlantaReciclaje> findByUbicacionContaining(String ubicacion);
    List<PlantaReciclaje> findByActivaTrue();
}
