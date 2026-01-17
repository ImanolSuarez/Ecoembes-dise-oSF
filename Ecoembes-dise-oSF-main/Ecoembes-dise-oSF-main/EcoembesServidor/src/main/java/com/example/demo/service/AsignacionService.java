package com.example.demo.service;

import com.example.demo.entity.Asignacion;
import com.example.demo.entity.Contenedor;
import com.example.demo.entity.Personal;
import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.factory.PlantaGatewayFactory;
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
    private final PlantaGatewayFactory plantaGatewayFactory;

    public AsignacionService(AsignacionRepository asignacionRepository,
            ContenedorRepository contenedorRepository,
            PlantaReciclajeRepository plantaRepository,
            PersonalRepository personalRepository,
            PlantaGatewayFactory plantaGatewayFactory) {
        this.asignacionRepository = asignacionRepository;
        this.contenedorRepository = contenedorRepository;
        this.plantaRepository = plantaRepository;
        this.personalRepository = personalRepository;
        this.plantaGatewayFactory = plantaGatewayFactory;
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

        for (String contenedorId : contenedoresIds) {
            Optional<Contenedor> contOpt = contenedorRepository.findById(contenedorId);
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

        Asignacion savedAsignacion = asignacionRepository.save(asignacion);

        // Notificar a la planta externa creando Gateway directamente
        try {
            // El Service crea el Gateway directamente según el tipo de planta
            var gateway = crearGateway(planta.getNombre());

            // Crear DTO para la notificación
            com.example.demo.dto.AsignacionDTO dto = new com.example.demo.dto.AsignacionDTO(
                    savedAsignacion.getId(),
                    planta.getId(),
                    planta.getNombre(),
                    savedAsignacion.getContenedoresIds(),
                    savedAsignacion.getFecha(),
                    savedAsignacion.getTotalContenedores(),
                    savedAsignacion.getTotalEnvases(),
                    savedAsignacion.getPesoEstimado(),
                    empleado.getEmail(),
                    savedAsignacion.getFechaAsignacion());

            boolean notificado = gateway.notificarAsignacion(dto);
            if (!notificado) {
                // Podríamos loguear un warning o intentar reintentar
                System.err.println("ADVERTENCIA: No se pudo notificar a la planta externa " + planta.getNombre());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Fallo al intentar comunicar con la planta externa: " + e.getMessage());
        }

        return Optional.of(savedAsignacion);
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
