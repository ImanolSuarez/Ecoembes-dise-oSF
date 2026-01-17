package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Asignacion;
import java.time.LocalDate;
import java.util.List;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByEmpleadoResponsableEmail(String email);

    List<Asignacion> findByPlantaId(Long plantaId);

    List<Asignacion> findByFecha(LocalDate fecha);

    List<Asignacion> findByFechaBetween(LocalDate inicio, LocalDate fin);
}
