package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.exception.AppointmentNotFoundException;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.AppointmentMapper;
import com.example.GastroProject.mapper.DoctorMapper;
import com.example.GastroProject.repository.AppointmentRepository;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.AppointmentService;
import com.example.GastroProject.service.DoctorService;
import com.example.GastroProject.service.PatientService;
import com.example.GastroProject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentMapper appointmentMapper;


    @Override
    public void saveAppointmentForPatient(AppointmentDto newAppointmentDto, String email, Long doctorId) {
        Patient patient = patientRepository.findByEmail(email);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Appointment newAppointment = appointmentMapper.DTOToEntity(newAppointmentDto);
        newAppointment.setAppointmentDate(newAppointmentDto.getAppointmentDate());
        newAppointment.setAppointmentTime(newAppointmentDto.getAppointmentTime());
        newAppointment.setDoctor(newAppointmentDto.getDoctor());

        patient.addAppointment(newAppointment);
        doctor.addAppointment(newAppointment);

        patientRepository.save(patient);
        doctorRepository.save(doctor);
    }

    @Override
    public void saveAppointmentForDoctor(AppointmentDto newAppointmentDto, String doctorEmail, Long patientId) {
        Doctor doctor = doctorRepository.findByEmail(doctorEmail);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentDate(newAppointmentDto.getAppointmentDate());
        newAppointment.setAppointmentTime(newAppointmentDto.getAppointmentTime());
        newAppointment.setPatient(newAppointmentDto.getPatient());

        doctor.addAppointment(newAppointment);
        patient.addAppointment(newAppointment);

        doctorRepository.save(doctor);
        patientRepository.save(patient);
    }


    @Override
    public Optional<AppointmentDto> findById(Long id) {
        var optionalAppointment = appointmentRepository.findById(id);
        return optionalAppointment.map(appointmentMapper::entityToDTO);
    }

    @Override
    public void updateAppointmentForPatient(AppointmentDto updatedAppointment) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(updatedAppointment.getId());

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
            appointment.setDoctor(updatedAppointment.getDoctor());


            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment with ID " + updatedAppointment.getId() + " not found");
        }
    }





    @Override
    public List<AppointmentDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate) {
        List<Appointment> appointments = appointmentRepository.findByPatient(patient, Sort.by(Sort.Direction.DESC, "appointmentDate"));

        return appointments.stream()
                .filter(appointment -> (selectedDate == null || appointment.getAppointmentDate().equals(selectedDate)) &&
                        (keyword == null ||
                                appointment.getDoctor().getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                appointment.getDoctor().getSpecialization().toLowerCase().contains(keyword.toLowerCase())))
                .map(appointment -> {
                    AppointmentDto appointmentDto = appointmentMapper.entityToDTO(appointment);
                    appointmentDto.setDoctorName(appointment.getDoctor().getName());
                    appointmentDto.setSpecialization(appointment.getDoctor().getSpecialization());
                    return appointmentDto;
                })
                .toList();
    }


    @Override
    public List<AppointmentDto> findByDoctorAndKeywordAndDate(Doctor doctor, String keyword, LocalDate selectedDate) {
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor, Sort.by(Sort.Direction.DESC, "appointmentDate"));
        return appointments.stream()
                .filter(appointment -> (selectedDate == null || appointment.getAppointmentDate().equals(selectedDate)) &&
                        (keyword == null ||
                                appointment.getPatient().getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                appointment.getPatient().getDiagnosis().toLowerCase().contains(keyword.toLowerCase())))
                .map(appointment -> {
                    AppointmentDto appointmentDto = appointmentMapper.entityToDTO(appointment);
                    appointmentDto.setPatientName(appointment.getPatient().getName());
                    appointmentDto.setDiagnosis(appointment.getPatient().getDiagnosis());
                    return appointmentDto;
                })
                .toList();
    }

    @Override
    public void updateAppointmentForDoctor(AppointmentDto updatedAppointment) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(updatedAppointment.getId());

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
            appointment.setPatient(updatedAppointment.getPatient());


            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment with ID " + updatedAppointment.getId() + " not found");
        }
    }

    @Override
    public void deleteAppointmentForDoctor(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            Patient patient = appointment.getPatient();

            appointment.getDoctor().removeAppointment(appointment);
            patient.removeAppointment(appointment);


            appointmentRepository.delete(appointment);

        } else {
            throw new IllegalArgumentException("Doctor or Patient associated with the appointment is null");
        }
    }

    @Override
    public void deleteAppointmentforPatient(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            Doctor doctor = appointment.getDoctor();

            appointment.getPatient().removeAppointment(appointment);
            doctor.removeAppointment(appointment);


            appointmentRepository.delete(appointment);

        } else {
            throw new IllegalArgumentException("Doctor or Patient associated with the appointment is null");
        }
    }
}






