package com.example.GastroProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private Integer age;

    private Integer height;

    private Integer weight;

    private String diagnosis;

    private String allergies;
}
