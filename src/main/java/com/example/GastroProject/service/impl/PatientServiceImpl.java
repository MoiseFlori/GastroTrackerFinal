package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.repository.*;
import com.example.GastroProject.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PatientProfileRepository patientProfileRepository;


    @Override
    @Transactional
    public void savePatient(PatientDto patientDto) {

        User user = new User();
        user.setName(patientDto.getName());
        user.setEmail(patientDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(patientDto.getPassword()));
        user.setRoles(patientDto.getRoles());
        user.getRoles().forEach(role -> {
            final Role roleByName = roleRepository.findByName(role.getName());
            role.setId(Objects.requireNonNullElseGet(roleByName, () -> roleRepository.save(role)).getId());
        });
        userRepository.save(user);

        Patient patient = new Patient();
        patient.setName(patientDto.getName());
        patient.setEmail(patientDto.getEmail());
        patient.setAge(patientDto.getAge());
        patient.setDiagnosis(patientDto.getDiagnosis());
        patient.setUser(user);

        Long doctorId = patientDto.getDoctorId();
        if (doctorId != null) {
            Doctor selectedDoctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"));

            patient.setDoctor(selectedDoctor);
            selectedDoctor.getPatients().add(patient);
        }

        patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
    }

    @Override
    public PatientProfile getPatientProfileById(Long patientId) {
        Optional<PatientProfile> patientProfileOptional = patientProfileRepository.findById(patientId);

        if (patientProfileOptional.isPresent()) {
            return patientProfileOptional.get();
        } else {
            throw new RuntimeException("Patient Profile not found for id: " + patientId);
        }
    }


}

