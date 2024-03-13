package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.PatientProfileDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.PatientProfileRepository;
import com.example.GastroProject.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientRepository patientRepository;

    private final PatientProfileRepository patientProfileRepository;


    @Override
    public PatientProfile savePatientProfile(PatientProfileDto patientProfileDto) {
        Optional<PatientProfile> optionalUserProfile = patientProfileRepository.findByEmail(patientProfileDto.getEmail());

        return optionalUserProfile.map(existingUserProfile -> {
            // Actualizeaza profilul existent cu noile date
            existingUserProfile.setFirstName(patientProfileDto.getFirstName());
            existingUserProfile.setLastName(patientProfileDto.getLastName());
            existingUserProfile.setEmail(patientProfileDto.getEmail());
            existingUserProfile.setBirthDate(patientProfileDto.getBirthDate());
            existingUserProfile.setAge(patientProfileDto.getAge());
            existingUserProfile.setHeight(patientProfileDto.getHeight());
            existingUserProfile.setWeight(patientProfileDto.getWeight());
            existingUserProfile.setDiagnosis(patientProfileDto.getDiagnosis());
            existingUserProfile.setAllergies(patientProfileDto.getAllergies());

            // Salveaza profilul actualizat in baza de date
            PatientProfile savedProfile = patientProfileRepository.save(existingUserProfile);

            // Adauga profilul la utilizator, daca nu are deja unul
            if (savedProfile.getPatient() != null) {
                savedProfile.getPatient().addProfile(savedProfile);
                patientRepository.save(savedProfile.getPatient());
            }

            return savedProfile;

        }).orElseGet(() -> {
            // Returnează un profil nou cu datele primite daca nu exista unul existent
            PatientProfile newProfile = new PatientProfile();
            newProfile.setEmail(patientProfileDto.getEmail());
            newProfile.setFirstName(patientProfileDto.getFirstName());
            newProfile.setLastName(patientProfileDto.getLastName());
            newProfile.setBirthDate(patientProfileDto.getBirthDate());
            newProfile.setAge(patientProfileDto.getAge());
            newProfile.setHeight(patientProfileDto.getHeight());
            newProfile.setWeight(patientProfileDto.getWeight());
            newProfile.setDiagnosis(patientProfileDto.getDiagnosis());
            newProfile.setAllergies(patientProfileDto.getAllergies());

            // Salveaza noul profil in baza de date
            PatientProfile savedProfile = patientProfileRepository.save(newProfile);

            // Adauga profilul la utilizator
            if (savedProfile.getPatient() != null) {
                savedProfile.getPatient().addProfile(savedProfile);
                patientRepository.save(savedProfile.getPatient());
            }

            return savedProfile;
        });
    }


    public PatientProfile getPatientProfileByEmail(String userEmail) {
        Patient patient = patientRepository.findByEmail(userEmail);

        PatientProfile patientProfile = patient.getPatientProfile();

        if (patientProfile == null) {
            patientProfile = new PatientProfile();
            patientProfile.setEmail(userEmail);
            patient.addProfile(patientProfile);
            patientRepository.save(patient);
        }

        return patientProfile;
    }


}





