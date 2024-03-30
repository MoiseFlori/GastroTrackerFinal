package com.example.GastroProject.dto;

import com.example.GastroProject.entity.Administration;
import com.example.GastroProject.entity.MedicineType;
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
public class TreatmentDto {

    private Long id;

    private String name;

    private String dose;

    private MedicineType medicineType;

    private Administration administration;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTreatment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTreatment;

    private String description;

    private Integer durationInDays;

    private Long patientId;

    private boolean finished;


}

