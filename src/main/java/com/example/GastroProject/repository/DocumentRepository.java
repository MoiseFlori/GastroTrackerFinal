package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Document;
import com.example.GastroProject.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {


    List<Document> findByPatient(Patient patient);
    @Query("SELECT d FROM Document d WHERE d.patient.id = :patientId")
    List<Document> findByPatientId(Long patientId);

}
