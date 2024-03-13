package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRegistrationDataLoaderService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Transactional
    public void loadUserDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] userData = line.split(",");
                User user = new User();
                user.setName(userData[0]);
                user.setEmail(userData[1]);
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(userData[2]);
                user.setPassword(hashedPassword);

                Set<Role> roles = new HashSet<>();
                for (int i = 3; i < userData.length; i++) {
                    String roleName = userData[i].trim();
                    Role role = roleRepository.findByName(roleName);
                    if (role != null) {
                        roles.add(role);
                    }
                }
                user.setRoles(roles);

                userRepository.save(user);
            }
            System.out.println("Data loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }



}
