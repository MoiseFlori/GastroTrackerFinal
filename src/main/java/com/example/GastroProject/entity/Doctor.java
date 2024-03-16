package com.example.GastroProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    private String name;

    private String email;

    private String stamp;

    private String specialization;


    @OneToMany(mappedBy = "doctor",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorSchedule> schedules = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    public void addAppointment(Appointment appointment) {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
        appointment.setDoctor(this);
    }
    public void addPatient(Patient patient) {
        if (patients == null) {
            patients = new ArrayList<>();
        }
        patients.add(patient);
        patient.setDoctor(this);
    }

    public void removePatient(Patient patient){

        patients.remove(patient);
        patient.setDoctor(null);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setDoctor(null);

    }
}
