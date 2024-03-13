package com.example.GastroProject.dto;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
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

    private Long doctorId;

    private Long patientId;

    private String patientName;

    private String diagnosis;

    private String doctorName;

    private String specialization;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private Patient patient;

    private Doctor doctor;
}
