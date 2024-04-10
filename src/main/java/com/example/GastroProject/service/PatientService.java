package com.example.GastroProject.service;

import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.PatientProfile;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {

    void savePatient(PatientDto patientDto);

    Patient getPatientById(Long patientId);

    PatientProfile getPatientProfileById(Long patientId);

    boolean existsByEmail(String email);
}
