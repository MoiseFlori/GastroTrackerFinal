package com.example.GastroProject.controller;

import com.example.GastroProject.dto.UserProfileDto;
import com.example.GastroProject.entity.UserProfile;
import com.example.GastroProject.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/user-profile")
    public String showProfilePage(Model model, Authentication authentication) {
        String userEmail = authentication.getName();
        UserProfile userProfile = userProfileService.getUserProfileByEmail(userEmail);
        model.addAttribute("userProfile", userProfile);
        return "user-profile";
    }

    @PostMapping("/user-profile")
    public String saveProfile(@ModelAttribute("userProfile") UserProfileDto userProfileDto, Authentication authentication) {
        String userEmail = authentication.getName();
        userProfileDto.setEmail(userEmail);
        UserProfile savedProfile = userProfileService.saveUserProfile(userProfileDto);
        return "user";
    }

}
