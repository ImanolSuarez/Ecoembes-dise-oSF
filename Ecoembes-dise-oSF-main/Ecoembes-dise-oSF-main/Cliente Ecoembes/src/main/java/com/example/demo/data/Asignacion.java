package com.example.demo.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record Asignacion(
    Long id,
    Long plantaId,
    String plantaNombre,
    List<String> contenedoresIds,
    LocalDate fecha,
    int totalContenedores,
    int totalEnvases,
    float pesoEstimado,
    String empleadoResponsable,
    LocalDateTime fechaAsignacion
) {
    public Asignacion(Long plantaId, List<String> contenedoresIds, LocalDate fecha, String empleadoResponsable) {
        this(null, plantaId, null, contenedoresIds, fecha, 0, 0, 0.0f, empleadoResponsable, LocalDateTime.now());
    }
}
