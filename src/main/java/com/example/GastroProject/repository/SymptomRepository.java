package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SymptomRepository extends JpaRepository<Symptom,Long> {




    @Query("SELECT s FROM Symptom s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.severity) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))"

    )
    List<Symptom> findByKeyword(@Param("keyword") String keyword, Sort sort);

    List<Symptom> findByPatient(Patient patient, Sort localDatePart);
}
