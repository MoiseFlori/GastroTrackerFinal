package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDataLoaderService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public void loadPatientDataFromFile(String patientFilePath) {
        List<User> usersWithPatientRole = userRepository.findAllUsersWithRole("ROLE_PATIENT");

        try (BufferedReader patientReader = new BufferedReader(new FileReader(patientFilePath))) {
            patientReader.readLine();
            String line;
            int index = 0;

            while ((line = patientReader.readLine()) != null && index < usersWithPatientRole.size()) {
                String[] patientData = line.split(",");
                if (patientData.length >= 2) {
                    int age = Integer.parseInt(patientData[0].trim());
                    String diagnosis = patientData[1].trim();
                    Long doctorId = Long.parseLong(patientData[2].trim());
                    User user = usersWithPatientRole.get(index);
                    Patient patient = patientRepository.findByUserId(user.getId());

                    if (patient == null) {
                        patient = new Patient();
                        patient.setUser(user);
                    }

                    patient.setName(user.getName());
                    patient.setEmail(user.getEmail());
                    patient.setAge(age);
                    patient.setDiagnosis(diagnosis);

                    Doctor doctor = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"));

                    patient.setDoctor(doctor);
                    doctor.getPatients().add(patient);

                    patientRepository.save(patient);
                    index++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient data from file: " + e.getMessage());
        }
    }
}




