package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.PatientProfile;
import com.example.GastroProject.repository.PatientProfileRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientProfileDataLoaderService {

    private final PatientProfileRepository patientProfileRepository;


    private final PatientRepository patientRepository;

    @Transactional
    public void loadUserProfilesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;  // Ignoră prima linie
                }
                String[] patientProfileData = line.split(",");
                PatientProfile patientProfile = new PatientProfile();
                patientProfile.setFirstName(patientProfileData[0]);
                patientProfile.setLastName(patientProfileData[1]);
                patientProfile.setEmail(patientProfileData[2]);
                patientProfile.setBirthDate(LocalDate.parse(patientProfileData[3]));
                patientProfile.setAge(Integer.parseInt(patientProfileData[4]));
                patientProfile.setHeight(Integer.parseInt(patientProfileData[5]));
                patientProfile.setWeight(Integer.parseInt(patientProfileData[6]));
                patientProfile.setDiagnosis(patientProfileData[7]);
                patientProfile.setAllergies(patientProfileData[8]);

                // Asigură-te că user-ul există înainte de a-l seta
                Patient patient = patientRepository.findByEmail(patientProfileData[9]);
                if (patient != null) {
                    patientProfile.setPatient(patient);
                }

                patientProfileRepository.save(patientProfile);
            }
            System.out.println("Patient profiles loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
}
