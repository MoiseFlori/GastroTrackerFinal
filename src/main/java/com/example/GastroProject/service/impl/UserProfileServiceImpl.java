package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.UserProfileDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.mapper.UserProfileMapper;
import com.example.GastroProject.repository.UserProfileRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;


    @Override
    public UserProfile saveUserProfile(UserProfileDto userProfileDto) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(userProfileDto.getEmail());

        return optionalUserProfile.map(existingUserProfile -> {
            // Actualizeaza profilul existent cu noile date
            existingUserProfile.setFirstName(userProfileDto.getFirstName());
            existingUserProfile.setLastName(userProfileDto.getLastName());
            existingUserProfile.setEmail(userProfileDto.getEmail());
            existingUserProfile.setBirthDate(userProfileDto.getBirthDate());
            existingUserProfile.setAge(userProfileDto.getAge());
            existingUserProfile.setHeight(userProfileDto.getHeight());
            existingUserProfile.setWeight(userProfileDto.getWeight());
            existingUserProfile.setDiagnosis(userProfileDto.getDiagnosis());
            existingUserProfile.setAllergies(userProfileDto.getAllergies());

            // Salveaza profilul actualizat in baza de date
            UserProfile savedProfile = userProfileRepository.save(existingUserProfile);

            // Adauga profilul la utilizator, daca nu are deja unul
            if (savedProfile.getUser() != null) {
                savedProfile.getUser().addProfile(savedProfile);
                userRepository.save(savedProfile.getUser());
            }

            return savedProfile;

        }).orElseGet(() -> {
            // Returnează un profil nou cu datele primite daca nu exista unul existent
            UserProfile newProfile = new UserProfile();
            newProfile.setEmail(userProfileDto.getEmail());
            newProfile.setFirstName(userProfileDto.getFirstName());
            newProfile.setLastName(userProfileDto.getLastName());
            newProfile.setBirthDate(userProfileDto.getBirthDate());
            newProfile.setAge(userProfileDto.getAge());
            newProfile.setHeight(userProfileDto.getHeight());
            newProfile.setWeight(userProfileDto.getWeight());
            newProfile.setDiagnosis(userProfileDto.getDiagnosis());
            newProfile.setAllergies(userProfileDto.getAllergies());

            // Salveaza noul profil in baza de date
            UserProfile savedProfile = userProfileRepository.save(newProfile);

            // Adauga profilul la utilizator
            if (savedProfile.getUser() != null) {
                savedProfile.getUser().addProfile(savedProfile);
                userRepository.save(savedProfile.getUser());
            }

            return savedProfile;
        });
    }


    public UserProfile getUserProfileByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail);

        UserProfile userProfile = user.getUserProfile();

        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setEmail(userEmail);
            user.addProfile(userProfile);
            userRepository.save(user);
        }

        return userProfile;
    }


}





