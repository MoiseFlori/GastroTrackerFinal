package com.example.GastroProject.service;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.User;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface AppointmentService {



    void saveAppointmentForPatient(AppointmentDto appointmentDto, String email, Long doctorId);

    void saveAppointmentForDoctor(AppointmentDto appointmentDto, String name, Long patientId);

    Optional<AppointmentDto> findById(Long id);

    void updateAppointmentForPatient(AppointmentDto updatedAppointment);


    List<AppointmentDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate);


    List<AppointmentDto> findByDoctorAndKeywordAndDate(Doctor doctor, String keyword, LocalDate selectedDate);

    void updateAppointmentForDoctor(AppointmentDto updatedAppointment);

    void deleteAppointmentForDoctor(Long appointmentId);

    void deleteAppointmentForPatient(Long id);

    List<LocalTime> getAvailableSlots(Long doctorId, DayOfWeek dayOfWeek);


}
