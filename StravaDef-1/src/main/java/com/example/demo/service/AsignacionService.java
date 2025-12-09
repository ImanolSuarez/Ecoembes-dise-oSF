package com.example.demo.service;

import com.example.demo.entity.Asignacion;
import com.example.demo.entity.Contenedor;
import com.example.demo.entity.Personal;
import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.repository.AsignacionRepository;
import com.example.demo.repository.ContenedorRepository;
import com.example.demo.repository.PersonalRepository;
import com.example.demo.repository.PlantaReciclajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final ContenedorRepository contenedorRepository;
    private final PlantaReciclajeRepository plantaRepository;
    private final PersonalRepository personalRepository;

    public AsignacionService(AsignacionRepository asignacionRepository,
                           ContenedorRepository contenedorRepository,
                           PlantaReciclajeRepository plantaRepository,
                           PersonalRepository personalRepository) {
        this.asignacionRepository = asignacionRepository;
        this.contenedorRepository = contenedorRepository;
        this.plantaRepository = plantaRepository;
        this.personalRepository = personalRepository;
    }

    public List<Asignacion> findAll() {
        return asignacionRepository.findAll();
    }

    public Optional<Asignacion> findById(Long id) {
        return asignacionRepository.findById(id);
    }

    public Optional<Asignacion> create(Long plantaId, List<String> contenedoresIds, 
                                      LocalDate fecha, String empleadoEmail) {
        
        // Verificar que la planta existe
        Optional<PlantaReciclaje> plantaOpt = plantaRepository.findById(plantaId);
        if (plantaOpt.isEmpty()) {
            return Optional.empty();
        }
        
        // Verificar que el empleado existe
        Optional<Personal> empleadoOpt = personalRepository.findById(empleadoEmail);
        if (empleadoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        PlantaReciclaje planta = plantaOpt.get();
        Personal empleado = empleadoOpt.get();
        
        // Crear asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setPlanta(planta);
        asignacion.setEmpleadoResponsable(empleado);
        asignacion.setFecha(fecha);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        
        // Procesar contenedores
        int totalContenedores = 0;
        int totalEnvases = 0;
        float pesoEstimado = 0.0f;
        
        for (String contenedorId : contenedoresIds) { Optional<Contenedor> contOpt = contenedorRepository.findById(contenedorId);
            if (contOpt.isPresent()) {
            Contenedor contenedor = contOpt.get();
            asignacion.addContenedor(contenedor.getId());

            // Asignar contenedor a la planta
            contenedor.setPlantaAsignada(planta);
            contenedorRepository.save(contenedor);

            totalContenedores++;
            totalEnvases += contenedor.getNumEnvases();
            pesoEstimado += contenedor.calcularPesoEstimado() / 1000f; // convertir kg a toneladas
    }
}
        
        asignacion.setTotalContenedores(totalContenedores);
        asignacion.setTotalEnvases(totalEnvases);
        asignacion.setPesoEstimado(pesoEstimado);
        
        return Optional.of(asignacionRepository.save(asignacion));
    }

    public boolean delete(Long id) {
        if (!asignacionRepository.existsById(id)) {
            return false;
        }
        asignacionRepository.deleteById(id);
        return true;
    }

    public List<Asignacion> findByEmpleado(String email) {
        return asignacionRepository.findByEmpleadoResponsableEmail(email);
    }

    public List<Asignacion> findByPlanta(Long plantaId) {
        return asignacionRepository.findByPlantaId(plantaId);
    }

    public List<Asignacion> findByFecha(LocalDate fecha) {
        return asignacionRepository.findByFecha(fecha);
    }

    public List<Asignacion> findByFechaRango(LocalDate inicio, LocalDate fin) {
        return asignacionRepository.findByFechaBetween(inicio, fin);
    }
}
