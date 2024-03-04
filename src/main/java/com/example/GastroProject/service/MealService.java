package com.example.GastroProject.service;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface MealService {
    //List<MealDto> getAllMeals();

    void addMeal(MealDto mealDto, String email);

    Optional<MealDto> findById(Long id);

    void updateMeal(MealDto updatedMeal);

    void deleteMeal(Long id);

    //List<MealDto> findByKeyword(String keyword);

    List<MealDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate);
}
