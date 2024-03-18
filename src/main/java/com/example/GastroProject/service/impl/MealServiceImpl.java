package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.exception.MealNotFoundException;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.MealMapper;
import com.example.GastroProject.mapper.SymptomMapper;
import com.example.GastroProject.repository.MealRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.SymptomRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final PatientRepository patientRepository;

    private final MealRepository mealRepository;

    private final MealMapper mealMapper;


//    @Override
//    public List<MealDto> getAllMeals() {
//        List<Meal> meals = mealRepository.findAll();
//        return meals.stream()
//                .map(mealMapper::entityToDTO)
//                .toList();
//    }

    @Override
    public void addMeal(MealDto mealDto, String email) {
        Patient patient = patientRepository.findByEmail(email);
        Meal meal = mealMapper.DTOToEntity(mealDto);
        patient.addMeal(meal);
        patientRepository.save(patient);
    }

    @Override
    public Optional<MealDto> findById(Long id) {
        var optionalMeal = mealRepository.findById(id);
        return optionalMeal.map(mealMapper::entityToDTO);
    }

    @Override
    public void updateMeal(Long id,MealDto updatedMeal) {
        Optional<Meal> optionalMeal = mealRepository.findById(id);

        if (optionalMeal.isPresent()) {
            Meal existingMeal = optionalMeal.get();
            existingMeal.setLocalDatePart(updatedMeal.getLocalDatePart());
            existingMeal.setLocalTimePartForBreakfast(updatedMeal.getLocalTimePartForBreakfast());
            existingMeal.setBreakfast(updatedMeal.getBreakfast());
            existingMeal.setLocalTimePartForLunch(updatedMeal.getLocalTimePartForLunch());
            existingMeal.setLunch(updatedMeal.getLunch());
            existingMeal.setLocalTimePartForDinner(updatedMeal.getLocalTimePartForDinner());
            existingMeal.setDinner(updatedMeal.getDinner());

            mealRepository.save(existingMeal);
        } else {
            throw new MealNotFoundException("Meal with ID " + updatedMeal.getId() + " not found");
        }
    }

    @Override
    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }


    @Override
    public List<MealDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate) {
        List<Meal> meals = mealRepository.findByPatient(patient, Sort.by(Sort.Direction.DESC, "localDatePart"));
        return meals.stream()
                .filter(meal -> (selectedDate == null || meal.getLocalDatePart().equals(selectedDate)) &&
                                (keyword == null ||
                        meal.getBreakfast().toLowerCase().contains(keyword.toLowerCase()) ||
                        meal.getLunch().toLowerCase().contains(keyword.toLowerCase()) ||
                        meal.getDinner().toLowerCase().contains(keyword.toLowerCase())))
                .map(mealMapper::entityToDTO)
                .toList();
    }
}





