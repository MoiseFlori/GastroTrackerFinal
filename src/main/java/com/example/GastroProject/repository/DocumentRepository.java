package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Document;
import com.example.GastroProject.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {


    List<Document> findByPatient(Patient patient);
}
