package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient,Long> {

   
    Patient findByUserId(Long id);


    Patient findByEmail(String email);

    boolean existsByEmail(String email);
}
