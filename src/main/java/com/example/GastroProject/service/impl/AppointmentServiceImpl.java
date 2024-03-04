package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.entity.UserProfile;
import com.example.GastroProject.exception.AppointmentNotFoundException;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.AppointmentMapper;
import com.example.GastroProject.repository.AppointmentRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.AppointmentService;
import com.example.GastroProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final AppointmentMapper appointmentMapper;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void saveAppointment(AppointmentDto appointmentDto, String email) {
        User user = userRepository.findByEmail(email);
        Appointment appointment = appointmentMapper.DTOToEntity(appointmentDto);
        user.addAppointment(appointment);
        userRepository.save(user);
    }

    @Override
    public Optional<AppointmentDto> findById(Long id) {
        var optionalAppointment = appointmentRepository.findById(id);
        return optionalAppointment.map(appointmentMapper::entityToDTO);
    }

    @Override
    public void updateAppointment(AppointmentDto updatedAppointment) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(updatedAppointment.getId());

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
            appointment.setSpecialization(updatedAppointment.getSpecialization());
            appointment.setDoctorName(updatedAppointment.getDoctorName());

            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment with ID " + updatedAppointment.getId() + " not found");
        }
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate) {
        List<Appointment> appointments = appointmentRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "appointmentDate"));
        return appointments.stream()
                .filter(appointment -> (selectedDate == null || appointment.getAppointmentDate().equals(selectedDate)) &&
                        (keyword == null ||
                                appointment.getDoctorName().toLowerCase().contains(keyword.toLowerCase()) ||
                                appointment.getSpecialization().toLowerCase().contains(keyword.toLowerCase())))
                .map(appointmentMapper::entityToDTO)
                .toList();
    }




}




