package com.example.GastroProject.controller;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final PatientRepository patientRepository;

    @GetMapping("/home1")
    public String userPage() {
        return "user";
    }

    @GetMapping("/all-meals")
    public String showAllMeals(Model model,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
       Patient patient = patientRepository.findByEmail(email);
        List<MealDto> meals = mealService.findByPatientAndKeywordAndDate(patient, keyword,selectedDate);
        model.addAttribute("meals", Objects.requireNonNullElseGet(meals, ArrayList::new));
        return "all-meals";
    }


//    @GetMapping("/all-meals")
//    public String showAllMeals(Model model) {
//        List<MealDto> meals = mealService.getAllMeals();
//        model.addAttribute("meals", meals);
//        return "all-meals";
//    }


    @GetMapping("/add-meal")
    public String showMealForm(Model model) {
        MealDto attributeValue = new MealDto();
        model.addAttribute("meal", attributeValue);
        return "add-meal";
    }


    @PostMapping("/add-meal")
    public String addMeal(@ModelAttribute("meal") MealDto mealDto, Principal principal) {
        LocalDate datePart = mealDto.getLocalDatePart();
        LocalTime timePart = mealDto.getLocalTimePartForBreakfast();
        LocalTime timePart1 = mealDto.getLocalTimePartForLunch();
        LocalTime timePart2 = mealDto.getLocalTimePartForDinner();
        mealService.addMeal(mealDto, principal.getName());
        return "redirect:/all-meals";
    }

    @GetMapping("/edit-meal/{id}")
    public String showEditMealForm(@PathVariable Long id, Model model) {
        model.addAttribute("meal", mealService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid meal Id:" + id)));
        return "edit-meal";
    }


    @PostMapping("/edit-meal/{id}")
    public String updateMeal(@PathVariable Long id, @ModelAttribute("meal") MealDto updatedMeal) {
        updatedMeal.setId(id);
        LocalDate datePart = updatedMeal.getLocalDatePart();
        LocalTime timePart = updatedMeal.getLocalTimePartForBreakfast();
        LocalTime timePart1 = updatedMeal.getLocalTimePartForLunch();
        LocalTime timePart2 = updatedMeal.getLocalTimePartForDinner();
        mealService.updateMeal(updatedMeal);
        return "redirect:/all-meals";
    }

    @GetMapping("/delete-meal/{id}")
    public String deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return "redirect:/all-meals";
    }
}
