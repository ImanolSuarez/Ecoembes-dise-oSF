package com.example.demo.service;

import com.example.demo.entity.Personal;
import com.example.demo.repository.PersonalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class PersonalService {

    private final PersonalRepository personalRepository;
    private final Random random = new Random();

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public List<Personal> getAllPersonal() {
        return personalRepository.findAll();
    }

    public Optional<Personal> getPersonalByEmail(String email) {
        return personalRepository.findById(email);
    }

    public Personal createPersonal(Personal personal) {
        if (personalRepository.existsById(personal.getEmail())) {
            return null; // Ya existe
        }

        if (personal.getFechaAlta() == null) {
            personal.setFechaAlta(LocalDate.now());
        }

        if (personal.getToken() == null) {
            personal.setToken(generarToken());
        }

        return personalRepository.save(personal);
    }

    public Optional<Personal> updatePersonal(String email, Personal cambios) {
        Optional<Personal> personalOpt = personalRepository.findById(email);
        
        if (personalOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Personal personal = personalOpt.get();
        
        if (cambios.getNombre() != null) personal.setNombre(cambios.getNombre());
        if (cambios.getFechaAlta() != null) personal.setFechaAlta(cambios.getFechaAlta());
        
        return Optional.of(personalRepository.save(personal));
    }

    public boolean deletePersonal(String email) {
        if (!personalRepository.existsById(email)) {
            return false;
        }
        personalRepository.deleteById(email);
        return true;
    }

    private Long generarToken() {
        return Math.abs(random.nextLong() % 1_000_000_000L);
    }
}
