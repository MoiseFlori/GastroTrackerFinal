package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import com.sun.jdi.PrimitiveValue;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// ... (importurile existente)

@Service
@RequiredArgsConstructor
public class PatientDataLoaderService {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository; // Adaugă repository-ul pentru Doctor

    @Transactional
    public void loadPatientDataFromFile(String patientFilePath) {
        List<User> usersWithPatientRole = userRepository.findAllUsersWithRole("ROLE_PATIENT");

        try (BufferedReader patientReader = new BufferedReader(new FileReader(patientFilePath))) {
            patientReader.readLine();
            String line;
            int index = 0; // Acest index va urmări utilizatorul curent care trebuie asociat cu datele pacientului

            while ((line = patientReader.readLine()) != null && index < usersWithPatientRole.size()) {
                String[] patientData = line.split(",");
                if (patientData.length >= 2) {
                    int age = Integer.parseInt(patientData[0].trim());
                    String diagnosis = patientData[1].trim();
                    Long doctorId = Long.parseLong(patientData[2].trim()); // Adaugă ID-ul medicului din fișier

                    User user = usersWithPatientRole.get(index);
                    Patient patient = patientRepository.findByUserId(user.getId());

                    if (patient == null) {
                        patient = new Patient();
                        patient.setUser(user); // Asociază utilizatorul cu pacientul
                    }

                    patient.setName(user.getName());
                    patient.setEmail(user.getEmail());
                    patient.setAge(age);
                    patient.setDiagnosis(diagnosis);

                    // Adaugă legătura cu medicul
                    Doctor doctor = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"));

                    patient.setDoctor(doctor);
                    doctor.getPatients().add(patient);

                    patientRepository.save(patient);
                    index++; // Treci la următorul utilizator cu rol de pacient
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient data from file: " + e.getMessage());
        }
    }
}




