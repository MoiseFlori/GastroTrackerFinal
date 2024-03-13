package com.example.GastroProject.controller;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.service.DoctorService;
import com.example.GastroProject.service.PatientService;
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
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/registration")
    public String getRegistrationPageForPatient(@ModelAttribute("patient") PatientDto patientDto, Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "registration";
    }


    @PostMapping("/registration")
    public String savePatient(@ModelAttribute("patient") @Valid PatientDto patientDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Doctor> doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
            return "registration";
        }

        Doctor selectedDoctor = doctorService.getDoctorById(patientDto.getDoctorId());
        if (selectedDoctor == null) {
            // Tratează cazul în care ID-ul medicului este invalid
            String errorMessage = "Invalid doctor ID. Please select a valid doctor.";
            model.addAttribute("errorMessage", errorMessage);
            List<Doctor> doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
            return "registration";
        }

        patientDto.setRoles(Collections.singleton(new Role(Constants.ROLE_PATIENT)));
        patientDto.setDoctor(selectedDoctor);
        patientService.savePatient(patientDto);

        model.addAttribute("message", "Registered Successfully!");
        return "redirect:/login";
    }



}
