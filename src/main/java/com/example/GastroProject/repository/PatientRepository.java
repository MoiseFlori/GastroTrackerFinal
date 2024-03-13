package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    Patient findByEmail(String email);

    List<Patient> findByUser(User user);

    Patient findByUserId(Long id);
}
