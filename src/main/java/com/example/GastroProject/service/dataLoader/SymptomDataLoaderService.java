package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Severity;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
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

    private final UserRepository userRepository;

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
                Symptom symptom = new Symptom();
                symptom.setName(symptomData[0]);
                symptom.setSeverity(getSeverityFromString(symptomData[1].trim()));
                symptom.setDescription(symptomData[2]);
                symptom.setLocalDatePart(LocalDate.parse(symptomData[3]));
                symptom.setLocalTimePart(LocalTime.parse(symptomData[4]));

                String userEmail = symptomData[5].trim().toLowerCase();
                User user = userRepository.findByEmail(userEmail);

                if (user != null) {
                    symptom.setUser(user);
                }
                symptomRepository.save(symptom);
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
