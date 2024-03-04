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
public class AppointmentDto {

    private Long id;

    private String doctorName;

    private String specialization;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    private LocalTime appointmentTime;
}
