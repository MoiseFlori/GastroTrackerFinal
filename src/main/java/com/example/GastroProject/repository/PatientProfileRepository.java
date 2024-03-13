package com.example.GastroProject.repository;

import com.example.GastroProject.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile,Long> {
    Optional<PatientProfile> findByEmail(String email);
}
