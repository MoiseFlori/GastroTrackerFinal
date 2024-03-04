package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal,Long> {

    @Query("SELECT m FROM Meal m WHERE " +
            "LOWER(m.breakfast) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.lunch) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.dinner) LIKE LOWER(CONCAT('%', :keyword, '%'))"

    )
    List<Meal> findByKeyword(String keyword, Sort localDatePart);

    List<Meal> findByUser(User user, Sort localDatePart);
}
