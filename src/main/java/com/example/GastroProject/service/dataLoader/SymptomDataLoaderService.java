package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Severity;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.SymptomRepository;
import com.example.GastroProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SymptomDataLoaderService {

    private final SymptomRepository symptomRepository;


    private final PatientRepository patientRepository;

    @Transactional
    public void loadSymptomsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] symptomData = line.split(",");

                // Verifică dacă array-ul conține suficiente elemente
                if (symptomData.length >= 6) {
                    Symptom symptom = new Symptom();
                    symptom.setName(symptomData[0]);
                    symptom.setSeverity(getSeverityFromString(symptomData[1].trim()));
                    symptom.setDescription(symptomData[2]);
                    symptom.setLocalDatePart(LocalDate.parse(symptomData[3]));
                    symptom.setLocalTimePart(LocalTime.parse(symptomData[4]));

                    String email = symptomData[5].trim().toLowerCase();
                    Patient patient = patientRepository.findByEmail(email);

                    if (patient != null) {
                        symptom.setPatient(patient);
                    }
                    symptomRepository.save(symptom);
                }
            }
            System.out.println("Symptoms loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }

    private Severity getSeverityFromString(String severityString) {
        for (Severity severity : Severity.values()) {
            if (severity.getDisplayName().equalsIgnoreCase(severityString)) {
                return severity;
            }
        }
        throw new IllegalArgumentException("Invalid severity: " + severityString);
    }

}
