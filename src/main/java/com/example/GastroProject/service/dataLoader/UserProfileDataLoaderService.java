package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.User;
import com.example.GastroProject.entity.UserProfile;
import com.example.GastroProject.repository.UserProfileRepository;
import com.example.GastroProject.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserProfileDataLoaderService {

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    @Transactional
    public void loadUserProfilesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;  // Ignoră prima linie
                }
                String[] userProfileData = line.split(",");
                UserProfile userProfile = new UserProfile();
                userProfile.setFirstName(userProfileData[0]);
                userProfile.setLastName(userProfileData[1]);
                userProfile.setEmail(userProfileData[2]);
                userProfile.setBirthDate(LocalDate.parse(userProfileData[3]));
                userProfile.setAge(Integer.parseInt(userProfileData[4]));
                userProfile.setHeight(Integer.parseInt(userProfileData[5]));
                userProfile.setWeight(Integer.parseInt(userProfileData[6]));
                userProfile.setDiagnosis(userProfileData[7]);
                userProfile.setAllergies(userProfileData[8]);

                // Asigură-te că user-ul există înainte de a-l seta
                User user = userRepository.findByEmail(userProfileData[9]);
                if (user != null) {
                    userProfile.setUser(user);
                }

                userProfileRepository.save(userProfile);
            }
            System.out.println("User profiles loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
}
