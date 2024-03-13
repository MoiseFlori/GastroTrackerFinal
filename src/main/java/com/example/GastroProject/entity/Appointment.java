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
public class Appointment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "patient_id")
    private Patient patient;


    public void setDoctorId(Long doctorId) {
        this.doctor = new Doctor();
        this.doctor.setDoctorId(doctorId);
    }

    public void setPatientId(Long patientId) {
        this.patient = new Patient();
        this.patient.setPatientId(patientId);
    }

}
