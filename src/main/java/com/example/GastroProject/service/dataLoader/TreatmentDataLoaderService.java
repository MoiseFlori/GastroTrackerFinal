package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.*;
import com.example.GastroProject.repository.TreatmentRepository;
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
public class TreatmentDataLoaderService {

    private final TreatmentRepository treatmentRepository;

    private final UserRepository userRepository;


    @Transactional
    public void loadTreatmentsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;  // Ignoră prima linie
                }
                String[] treatmentData = line.split(",");
                Treatment treatment = new Treatment();
                treatment.setLocalDatePart(LocalDate.parse(treatmentData[0]));
                treatment.setLocalTimePart(LocalTime.parse(treatmentData[1]));
                treatment.setName(treatmentData[2]);
                treatment.setMedicineType(getMedicineTypeFromString(treatmentData[3].trim()));
                treatment.setDose(treatmentData[4]);
                treatment.setAdministration(Administration.valueOf(treatmentData[5]));
                treatment.setDescription(treatmentData[6]);

                // Asigură-te că user-ul există înainte de a-l seta
                User user = userRepository.findByEmail(treatmentData[7]);
                if (user != null) {
                    treatment.setUser(user);
                }

                treatmentRepository.save(treatment);
            }
            System.out.println("Treatments loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
    private MedicineType getMedicineTypeFromString(String medicineTypeString) {
        for (MedicineType medicineType : MedicineType.values()) {
            if (medicineType.name().equalsIgnoreCase(medicineTypeString)) {
                return medicineType;
            }
        }
        throw new IllegalArgumentException("Invalid MedicineType: " + medicineTypeString);
    }

}
