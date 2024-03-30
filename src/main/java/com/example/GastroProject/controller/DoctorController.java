package com.example.GastroProject.controller;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.PatientProfile;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.service.DoctorService;
import com.example.GastroProject.service.PatientService;
import com.example.GastroProject.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping("/home4")
    public String doctorPage() {
        return "doctor-page";
    }

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

    @GetMapping("/patients")
    public String getPatientsPage(@AuthenticationPrincipal UserDetails userDetails, Model model, @RequestParam(required = false) String keyword){
        Doctor doctor = doctorService.getDoctorByEmail(userDetails.getUsername());
        List<Patient> patients;

        if (keyword != null && !keyword.isEmpty()) {
            patients = doctorService.searchDoctorPatientsByKeyword(doctor.getDoctorId(), keyword);
        } else {
            patients = doctorService.getDoctorPatients(doctor.getDoctorId());
        }
        model.addAttribute("doctor", doctor);
        model.addAttribute("patients", patients);
        return "dr-patients";
    }


    @GetMapping("/edit-patient/{id}")
    public String getEditPatientPage(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "edit-patient";
    }

    @PostMapping("/edit-patient/{id}")
    public String editPatient(@PathVariable Long id, @ModelAttribute("patient") PatientDto editedPatientDto) {
        doctorService.editPatientDiagnosis(id, editedPatientDto);
        return "redirect:/dr-patients";
    }


    @GetMapping("/delete-patient/{doctorId}/{patientId}")
    public String deletePatient(@PathVariable Long doctorId, @PathVariable Long patientId) {
        doctorService.dischargePatient(doctorId, patientId);
        return "redirect:/dr-patients";
    }

    @GetMapping("/patient-profile/{patientId}")
    public String viewPatientProfile(@PathVariable Long patientId, Model model) {
        PatientProfile patientProfile = patientService.getPatientProfileById(patientId);
        model.addAttribute("patient", patientProfile);
        return "view-patientProfile";
    }


}
