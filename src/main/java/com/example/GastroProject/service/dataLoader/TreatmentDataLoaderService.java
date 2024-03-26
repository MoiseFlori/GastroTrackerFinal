package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.*;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.TreatmentRepository;
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


    private final PatientRepository patientRepository;
    @Transactional
    public void loadTreatmentsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] treatmentData = line.split(",");
                Treatment treatment = new Treatment();
                treatment.setStartTreatment(LocalDate.parse(treatmentData[0]));
                treatment.setName(treatmentData[1]);
                treatment.setMedicineType(getMedicineTypeFromString(treatmentData[2].trim()));
                treatment.setDose(treatmentData[3]);
                treatment.setAdministration(Administration.valueOf(treatmentData[4]));
                treatment.setDescription(treatmentData[5]);
                int duration = Integer.parseInt(treatmentData[7]);
                treatment.setDurationInDays(duration);


                LocalDate endDate = treatment.getStartTreatment().plusDays(duration).minusDays(1);
                treatment.setEndTreatment(endDate);

                Patient patient = patientRepository.findByEmail(treatmentData[6]);
                if (patient != null) {
                    treatment.setPatient(patient);
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
