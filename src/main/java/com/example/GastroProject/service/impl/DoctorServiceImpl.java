package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.repository.*;
import com.example.GastroProject.service.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    @Transactional
    public void saveDoctor(DoctorDto doctorDto) {

        User user = new User();
        user.setName(doctorDto.getName());
        user.setEmail(doctorDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(doctorDto.getPassword()));
        user.setRoles((doctorDto.getRoles()));
        user.getRoles().forEach(role -> {
            final Role roleByName = roleRepository.findByName(role.getName());
            role.setId(Objects.requireNonNullElseGet(roleByName, () -> roleRepository.save(role)).getId());
        });
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setStamp(doctorDto.getStamp());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setUser(user);
        doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();

    }

    @Override
    public Doctor getDoctorById(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID: " + doctorId));
    }

    @Override
    public List<Patient> getDoctorPatients(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"));
        return doctor.getPatients();
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }


    @Override
    public void editPatient(Long patientId, PatientDto editedPatientDto) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        existingPatient.setDiagnosis(editedPatientDto.getDiagnosis());

        patientRepository.save(existingPatient);
    }


    @Override
    public void dischargePatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient with id " + patientId + " not found"));
        doctor.removePatient(patient);
        doctorRepository.save(doctor);
        patientRepository.save(patient);
    }

    @Override
    public List<Patient> searchDoctorPatientsByKeyword(Long doctorId, String keyword) {
        return doctorRepository.searchDoctorPatientsByKeyword(doctorId, keyword);
    }

    @Override
    public boolean isAvailable(Long doctorId, LocalDate date, LocalTime time) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek());

        if (doctorSchedule != null) {
            return !doctorSchedule.getStartTime().isBefore(time) || !doctorSchedule.getEndTime().isAfter(time);
        }
        return false;
    }

}




