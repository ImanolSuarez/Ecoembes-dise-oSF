package com.example.demo.repository;

import com.example.demo.entity.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, String> {

    
    Optional<Personal> findByToken(Long token);
}
