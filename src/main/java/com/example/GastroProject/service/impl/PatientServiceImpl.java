package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.DoctorService;
import com.example.GastroProject.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

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

}

