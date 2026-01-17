package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Contenedor;

import java.time.LocalDateTime;
import java.util.List;

public interface ContenedorRepository extends JpaRepository<Contenedor, String> {
    List<Contenedor> findByNivelLlenado(Contenedor.NivelLlenado nivel);
    List<Contenedor> findByUbicacionContaining(String ubicacion);
    List<Contenedor> findByPlantaAsignadaId(Long plantaId);

    List<Contenedor> findByNivelLlenadoAndUbicacionContaining(Contenedor.NivelLlenado nivel, String ubicacion);

    List<Contenedor> findByNivelLlenadoAndUltimaActualizacionBetween(
            Contenedor.NivelLlenado nivel, LocalDateTime inicio, LocalDateTime fin);

    List<Contenedor> findByNivelLlenadoAndUbicacionContainingAndUltimaActualizacionBetween(
            Contenedor.NivelLlenado nivel, String ubicacion, LocalDateTime inicio, LocalDateTime fin);
}
