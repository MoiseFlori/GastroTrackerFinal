package com.example.GastroProject.service;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.entity.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface MealService {

    void addMeal(MealDto mealDto, String email);

    Optional<MealDto> findById(Long id);

    void updateMeal(Long id,MealDto updatedMeal);

    void deleteMeal(Long id);

    List<MealDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate);
}
