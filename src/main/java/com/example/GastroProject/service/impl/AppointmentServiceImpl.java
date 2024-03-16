package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.exception.AppointmentNotAvailableException;
import com.example.GastroProject.exception.AppointmentNotFoundException;
import com.example.GastroProject.mapper.AppointmentMapper;
import com.example.GastroProject.repository.*;
import com.example.GastroProject.service.AppointmentService;
import com.example.GastroProject.service.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentMapper appointmentMapper;

    private final DoctorScheduleRepository doctorScheduleRepository;

    private final DoctorService doctorService;


    @Override
    public void saveAppointmentForPatient(AppointmentDto newAppointmentDto, String email, Long doctorId) {
        Patient patient = patientRepository.findByEmail(email);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));

        Appointment newAppointment = appointmentMapper.DTOToEntity(newAppointmentDto);
        newAppointment.setAppointmentDate(newAppointmentDto.getAppointmentDate());
        newAppointment.setAppointmentTime(newAppointmentDto.getAppointmentTime());

        DayOfWeek dayOfWeek = newAppointmentDto.getAppointmentDate().getDayOfWeek();
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);


        doctorSchedule.addAppointment(newAppointment);

        newAppointment.setDoctorSchedule(doctorSchedule);
        newAppointment.setDoctor(doctor);

        patient.addAppointment(newAppointment);
        doctor.addAppointment(newAppointment);

        patientRepository.save(patient);
        doctorRepository.save(doctor);
        doctorScheduleRepository.save(doctorSchedule);
    }

    @Override
    public void saveAppointmentForDoctor(AppointmentDto newAppointmentDto, String doctorEmail, Long patientId) {
        Doctor doctor = doctorRepository.findByEmail(doctorEmail);


        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Appointment newAppointment = appointmentMapper.DTOToEntity(newAppointmentDto);

        newAppointment.setAppointmentDate(newAppointmentDto.getAppointmentDate());
        newAppointment.setAppointmentTime(newAppointmentDto.getAppointmentTime());

        DayOfWeek dayOfWeek = newAppointmentDto.getAppointmentDate().getDayOfWeek();
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getDoctorId(), dayOfWeek);


        doctorSchedule.addAppointment(newAppointment);

        newAppointment.setDoctorSchedule(doctorSchedule);
        newAppointment.setDoctor(doctor);
        newAppointment.setPatient(patient);

        doctor.addAppointment(newAppointment);
        patient.addAppointment(newAppointment);

        doctorRepository.save(doctor);
        patientRepository.save(patient);
        doctorScheduleRepository.save(doctorSchedule);
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

            if (doctorService.isAvailable(updatedAppointment.getDoctor().getDoctorId(), updatedAppointment.getAppointmentDate(), updatedAppointment.getAppointmentTime())) {
                throw new AppointmentNotAvailableException("The selected date and time are not available for the doctor.");
            }

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
    public void deleteAppointmentForPatient(Long appointmentId) {
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


    @Override
    public List<LocalTime> getAvailableSlots(Long doctorId, DayOfWeek dayOfWeek) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
        if (doctorSchedule != null) {
            List<Appointment> appointments = doctorSchedule.getAppointments();
            LocalTime startTime;
            LocalTime endTime;

            switch (dayOfWeek) {
                case MONDAY, WEDNESDAY, FRIDAY:
                    startTime = LocalTime.of(7, 0);
                    endTime = LocalTime.of(15, 0);
                    break;
                case TUESDAY, THURSDAY:
                    startTime = LocalTime.of(15, 0);
                    endTime = LocalTime.of(21, 0);
                    break;
                default:
                    return Collections.emptyList();
            }

            List<LocalTime> availableSlots = new ArrayList<>();

            LocalTime currentTime = startTime;
            while (currentTime.isBefore(endTime)) {
                if (isSlotAvailable(currentTime, appointments)) {
                    availableSlots.add(currentTime);
                }
                currentTime = currentTime.plusMinutes(60);
            }

            return availableSlots;
        }
        return Collections.emptyList();
    }

    private boolean isSlotAvailable(LocalTime requestedTime, List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentTime().equals(requestedTime)) {
                return false;
            }
        }
        return true;
    }


}






