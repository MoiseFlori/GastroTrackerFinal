package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.Treatment;
import com.example.GastroProject.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment,Long> {
    @Query("SELECT t FROM Treatment t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.dose) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.medicineType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "  +
            "LOWER(t.administration) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))"

    )
    List<Treatment> findByKeyword(@Param("keyword") String keyword, Sort sort);

    List<Treatment> findByUser(User user, Sort localDatePart);
}


