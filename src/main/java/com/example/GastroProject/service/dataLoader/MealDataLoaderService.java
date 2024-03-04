package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.MealRepository;
import com.example.GastroProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MealDataLoaderService {

    private final MealRepository mealRepository;

    private final UserRepository userRepository;


    @Transactional
    public void loadMealsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] mealData = line.split(",");
                Meal meal = new Meal();
                meal.setLocalDatePart(LocalDate.parse(mealData[0]));
                meal.setLocalTimePartForBreakfast(LocalTime.parse(mealData[1]));
                meal.setBreakfast(mealData[2]);
                meal.setLocalTimePartForLunch(LocalTime.parse(mealData[3]));
                meal.setLunch(mealData[4]);
                meal.setLocalTimePartForDinner(LocalTime.parse(mealData[5]));
                meal.setDinner(mealData[6]);

                User user = userRepository.findByEmail(mealData[7]);
                if (user != null) {
                    meal.setUser(user);
                }

                mealRepository.save(meal);
            }
            System.out.println("Meals loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
}
