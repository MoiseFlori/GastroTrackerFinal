package com.example.GastroProject.service;

import com.example.GastroProject.dto.PatientProfileDto;
import com.example.GastroProject.entity.PatientProfile;
import org.springframework.stereotype.Service;

@Service
public interface PatientProfileService {

    PatientProfile savePatientProfile(PatientProfileDto patientProfileDto);

    PatientProfile getPatientProfileByEmail(String email);

}
