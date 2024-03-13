package com.example.GastroProject.controller;

import com.example.GastroProject.dto.PatientProfileDto;
import com.example.GastroProject.entity.PatientProfile;
import com.example.GastroProject.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @GetMapping("/patient-profile")
    public String showProfilePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        PatientProfile patientProfile = patientProfileService.getPatientProfileByEmail(email);
        model.addAttribute("patientProfile", patientProfile);
        return "patient-profile";
    }

    @PostMapping("/patient-profile")
    public String saveProfile(@ModelAttribute("patientProfile") PatientProfileDto patientProfileDto, Authentication authentication) {
        String email = authentication.getName();
        patientProfileDto.setEmail(email);
        PatientProfile savedProfile = patientProfileService.savePatientProfile(patientProfileDto);
        return "user";
    }

}
