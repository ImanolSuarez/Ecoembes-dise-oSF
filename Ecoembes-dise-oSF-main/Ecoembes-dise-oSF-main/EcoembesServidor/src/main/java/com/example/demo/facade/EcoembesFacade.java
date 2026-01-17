package com.example.demo.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Asignacion;
import com.example.demo.entity.Contenedor;
import com.example.demo.entity.Personal;
import com.example.demo.entity.PlantaReciclaje;
import com.example.demo.service.AsignacionService;
import com.example.demo.service.ContenedorService;
import com.example.demo.service.LoginService;
import com.example.demo.service.PersonalService;
import com.example.demo.service.PlantaReciclajeService;
import com.example.demo.service.SessionStateManager;

@Component
public class EcoembesFacade {

    private final LoginService loginService;
    private final PersonalService personalService;
    private final ContenedorService contenedorService;
    private final PlantaReciclajeService plantaService;
    private final AsignacionService asignacionService;
    private final SessionStateManager sessionStateManager;

    public EcoembesFacade(LoginService loginService,
            PersonalService personalService,
            ContenedorService contenedorService,
            PlantaReciclajeService plantaService,
            AsignacionService asignacionService,
            SessionStateManager sessionStateManager) {
        this.loginService = loginService;
        this.personalService = personalService;
        this.contenedorService = contenedorService;
        this.plantaService = plantaService;
        this.asignacionService = asignacionService;
        this.sessionStateManager = sessionStateManager;
    }

    // ==================== AUTENTICACIÓN ====================

    public Optional<String> login(String email, String password) {
        Optional<String> token = loginService.login(email, password);

        if (token.isPresent()) {
            sessionStateManager.createSession(token.get(), email);
        }

        return token;
    }

    public Optional<String> logout(String email) {
        Optional<String> result = loginService.logout(email);

        if (result.isPresent()) {
            sessionStateManager.removeSessionByEmail(email);
        }

        return result;
    }

    public Optional<String> logoutByToken(String token) {
        Optional<String> email = loginService.getEmailByToken(token);

        if (email.isPresent()) {
            Optional<String> result = loginService.logoutByToken(token);
            if (result.isPresent()) {
                sessionStateManager.removeSession(token);
            }
            return result;
        }

        return Optional.empty();
    }

    public Optional<Personal> register(String email, String password) {
        return loginService.register(email, password);
    }

    public boolean validateToken(String token) {
        return sessionStateManager.isSessionActive(token);
    }

    // ==================== PERSONAL ====================

    public List<Personal> getAllPersonal() {
        return personalService.getAllPersonal();
    }

    public Optional<Personal> getPersonalByEmail(String email) {
        return personalService.getPersonalByEmail(email);
    }

    public Personal createPersonal(Personal personal) {
        return personalService.createPersonal(personal);
    }

    public Optional<Personal> updatePersonal(String email, Personal personal) {
        return personalService.updatePersonal(email, personal);
    }

    public boolean deletePersonal(String email) {
        sessionStateManager.removeSessionByEmail(email);
        return personalService.deletePersonal(email);
    }

    // ==================== CONTENEDORES ====================

    public List<Contenedor> getAllContenedores() {
        return contenedorService.findAll();
    }

    public Optional<Contenedor> getContenedorById(String id) {
        return contenedorService.findById(id);
    }

    public Contenedor createContenedor(Contenedor contenedor) {
        return contenedorService.create(contenedor);
    }

    public Optional<Contenedor> updateContenedor(String id, Contenedor contenedor) {
        return contenedorService.update(id, contenedor);
    }

    public boolean deleteContenedor(String id) {
        return contenedorService.delete(id);
    }

    public List<Contenedor> getContenedoresByUbicacion(String ubicacion) {
        return contenedorService.findByUbicacion(ubicacion);
    }

    public List<Contenedor> getContenedoresByLlenado(Contenedor.NivelLlenado estado) {
        return contenedorService.findByNivelLlenado(estado);
    }

    public Optional<Contenedor> asignarContenedorAPlanta(String contenedorId, Long plantaId) {
        return contenedorService.asignarAPlanta(contenedorId, plantaId);
    }

    public List<Contenedor> getContenedoresByNivelYUbicacion(Contenedor.NivelLlenado nivel, String ubicacion) {
        return contenedorService.findByNivelYUbicacion(nivel, ubicacion);
    }

    public List<Contenedor> getContenedoresByNivelYFecha(Contenedor.NivelLlenado nivel, LocalDateTime inicio,
            LocalDateTime fin) {
        return contenedorService.findByNivelYFecha(nivel, inicio, fin);
    }

    public List<Contenedor> getContenedoresByNivelUbicacionYFecha(Contenedor.NivelLlenado nivel, String ubicacion,
            LocalDateTime inicio, LocalDateTime fin) {
        return contenedorService.findByNivelUbicacionYFecha(nivel, ubicacion, inicio, fin);
    }

    // ==================== PLANTAS ====================

    public List<PlantaReciclaje> getAllPlantas() {
        return plantaService.findAll();
    }

    public Optional<PlantaReciclaje> getPlantaById(Long id) {
        return plantaService.findById(id);
    }

    public PlantaReciclaje createPlanta(PlantaReciclaje planta) {
        return plantaService.create(planta);
    }

    public Optional<PlantaReciclaje> updatePlanta(Long id, PlantaReciclaje planta) {
        return plantaService.update(id, planta);
    }

    public boolean deletePlanta(Long id) {
        return plantaService.delete(id);
    }

    public com.example.demo.dto.CapacidadPlantaDTO getCapacidadRealPlanta(Long id, LocalDate fecha) {
        return plantaService.getCapacidadReal(id, fecha);
    }

    // ==================== ASIGNACIONES ====================

    public List<Asignacion> getAllAsignaciones() {
        return asignacionService.findAll();
    }

    public Optional<Asignacion> createAsignacion(Long plantaId, List<String> contenedoresIds,
            LocalDate fecha, String empleadoEmail) {
        Optional<Asignacion> asignacion = asignacionService.create(plantaId, contenedoresIds, fecha, empleadoEmail);

        if (asignacion.isPresent()) {
            sessionStateManager.logOperation(empleadoEmail, "CREATE_ASIGNACION",
                    "Asignación creada para planta " + plantaId);
        }

        return asignacion;
    }

    public boolean deleteAsignacion(Long id) {
        return asignacionService.delete(id);
    }

    public List<Asignacion> getAsignacionesByEmpleado(String email) {
        return asignacionService.findByEmpleado(email);
    }

    public List<Asignacion> getAsignacionesByPlanta(Long plantaId) {
        return asignacionService.findByPlanta(plantaId);
    }

    public List<Asignacion> getAsignacionesByFecha(LocalDate fecha) {
        return asignacionService.findByFecha(fecha);
    }

    // ==================== SESIONES ====================

    public List<String> getOperationHistory(String email) {
        return sessionStateManager.getOperationHistory(email);
    }

    public Optional<String> getSessionInfo(String token) {
        return sessionStateManager.getSessionInfo(token);
    }
}
