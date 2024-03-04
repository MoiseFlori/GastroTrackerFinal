package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.Symptom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealMapper {
    MealDto entityToDTO(Meal meal);

    @Mapping(target = "localDatePart", source = "localDatePart")
    @Mapping(target = "localTimePartForBreakfast", source = "localTimePartForBreakfast")
    @Mapping(target = "localTimePartForLunch", source = "localTimePartForLunch")
    @Mapping(target = "localTimePartForDinner", source = "localTimePartForDinner")
    Meal DTOToEntity(MealDto mealDto);
}
