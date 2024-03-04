package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.Treatment;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.exception.MealNotFoundException;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.MealMapper;
import com.example.GastroProject.mapper.SymptomMapper;
import com.example.GastroProject.repository.MealRepository;
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

    private final UserRepository userRepository;

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
        User user = userRepository.findByEmail(email);
        Meal meal = mealMapper.DTOToEntity(mealDto);
        user.addMeal(meal);
        userRepository.save(user);
    }

    @Override
    public Optional<MealDto> findById(Long id) {
        var optionalMeal = mealRepository.findById(id);
        return optionalMeal.map(mealMapper::entityToDTO);
    }

    @Override
    public void updateMeal(MealDto updatedMeal) {
        Optional<Meal> optionalMeal = mealRepository.findById(updatedMeal.getId());

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

//    @Override
//    public List<MealDto> findByKeyword(String keyword) {
//        if (keyword == null) {
//            return mealRepository.findAll(Sort.by(Sort.Direction.ASC, "localDatePart")).stream()
//                    .map(mealMapper::entityToDTO)
//                    .toList();
//
//        }
//        List<Meal> meals = mealRepository.findByKeyword(keyword, Sort.by(Sort.Direction.ASC, "localDatePart"));
//        return meals.stream()
//                .map(mealMapper::entityToDTO)
//                .toList();
//
//    }

    @Override
    public List<MealDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate) {
        List<Meal> meals = mealRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "localDatePart"));
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





