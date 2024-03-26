package com.example.GastroProject.repository;

import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByPatient(Patient patient, Sort appointmentDate);

    List<Appointment> findByDoctor(Doctor doctor, Sort appointmentDate);









}
