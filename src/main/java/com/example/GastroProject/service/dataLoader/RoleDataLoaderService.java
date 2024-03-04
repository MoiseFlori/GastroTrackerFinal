package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
                Role role = new Role();
                role.setName(roleName);

                roleRepository.save(role);
            }
            System.out.println("Roles loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }

}
