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
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String dose;

    @Enumerated(EnumType.STRING)
    private MedicineType medicineType;

    @Enumerated(EnumType.STRING)
    private Administration administration;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDatePart;

    private LocalTime localTimePart;
    private String description;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Patient patient;


}
