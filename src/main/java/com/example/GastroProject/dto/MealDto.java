package com.example.GastroProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {

    private Long id;

    private String breakfast;

    private String lunch;

    private String dinner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDatePart;

    private LocalTime localTimePartForBreakfast;

    private LocalTime localTimePartForLunch;

    private LocalTime localTimePartForDinner;
}
