package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorDataLoaderService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;


    @Transactional
    public void loadDoctorDataFromFile(String doctorFilePath) {
        List<User> usersWithPatientRole = userRepository.findAllUsersWithRole("ROLE_DOCTOR");

        try (BufferedReader doctorReader = new BufferedReader(new FileReader(doctorFilePath))) {
            doctorReader.readLine();
            String line;
            int index = 0;

            while ((line = doctorReader.readLine()) != null && index < usersWithPatientRole.size()) {
                String[] doctorData = line.split(",");
                if (doctorData.length >= 2) {
                    String specialization = doctorData[0].trim();
                    String stamp = doctorData[1].trim();

                    User user = usersWithPatientRole.get(index);
                    Doctor doctor = doctorRepository.findByUserId(user.getId());

                    if (doctor == null) {
                        doctor = new Doctor();
                        doctor.setUser(user);
                    }

                    doctor.setName(user.getName());
                    doctor.setEmail(user.getEmail());
                    doctor.setSpecialization(specialization);
                    doctor.setStamp(stamp);


                    doctorRepository.save(doctor);
                    index++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient data from file: " + e.getMessage());
        }
    }




}
