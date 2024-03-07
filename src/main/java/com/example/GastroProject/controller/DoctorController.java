package com.example.GastroProject.controller;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.service.DoctorService;
import com.example.GastroProject.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/registration-doctor")
    public String showDoctorRegistrationForm(@ModelAttribute("doctor") DoctorDto doctorDto) {
        return "registration-doctor";
    }

    @PostMapping("/registration-doctor")
    public String saveDoctor(@ModelAttribute("doctor") @Valid DoctorDto doctorDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration-doctor";
        }
        doctorDto.setRoles(Collections.singleton(new Role(Constants.ROLE_DOCTOR)));
        doctorService.saveDoctor(doctorDto);
        model.addAttribute("message", "Registered Successfully!");
        return "redirect:/login";
    }

}
