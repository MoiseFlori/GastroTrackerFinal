package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Role;
import com.example.GastroProject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleDataLoaderService {

    private final RoleRepository roleRepository;

    @Transactional
    public void loadRolesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String roleName = line.trim();

                // Verifică dacă rolul există deja
                Optional<Role> existingRole = roleRepository.findByName(roleName);
                if (existingRole.isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                } else {
                    System.out.println("Role " + roleName + " already exists, skipping...");
                }
            }
            System.out.println("Roles loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
}
