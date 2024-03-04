package com.example.GastroProject.service;

import com.example.GastroProject.dto.UserProfileDto;
import com.example.GastroProject.entity.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {

    UserProfile saveUserProfile(UserProfileDto userProfileDto);

    UserProfile getUserProfileByEmail(String userEmail);

}
