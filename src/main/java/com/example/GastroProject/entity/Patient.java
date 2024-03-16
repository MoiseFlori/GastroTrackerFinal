package com.example.GastroProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    private String name;

    private String email;

    private Integer age;

    private String diagnosis;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Symptom> symptoms;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Treatment> treatments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Meal> meals;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private PatientProfile patientProfile;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Document> documents;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    public void addSymptom(Symptom symptom) {
        symptoms.add(symptom);
        symptom.setPatient(this);
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
        treatment.setPatient(this);
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.setPatient(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setPatient(this);
    }

    public void addProfile(PatientProfile patientProfile) {
        this.patientProfile = patientProfile;
        patientProfile.setPatient(this);
    }


    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setPatient(null);
    }
}
