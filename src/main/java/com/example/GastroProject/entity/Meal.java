package com.example.GastroProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String breakfast;

    private String lunch;

    private String dinner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDatePart;

    private LocalTime localTimePartForBreakfast;

    private LocalTime localTimePartForLunch;

    private LocalTime localTimePartForDinner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;



}
