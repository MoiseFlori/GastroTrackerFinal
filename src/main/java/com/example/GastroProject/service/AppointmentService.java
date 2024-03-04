package com.example.GastroProject.service;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface AppointmentService {

    List<Appointment> getAllAppointments();

    void saveAppointment(AppointmentDto appointmentDto, String email);

    Optional<AppointmentDto> findById(Long id);

    void updateAppointment(AppointmentDto updatedAppointment);

    void deleteAppointment(Long id);

    List<AppointmentDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate);
}
