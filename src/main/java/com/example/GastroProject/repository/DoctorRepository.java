package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    Doctor findByEmail(String email);

    Doctor findByUserId(Long id);

    @Query("SELECT p FROM Doctor d JOIN d.patients p WHERE d.id = :doctorId AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.diagnosis) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Patient> searchDoctorPatientsByKeyword(@Param("doctorId") Long doctorId, @Param("keyword") String keyword);


    boolean existsByEmail(String email);
}
