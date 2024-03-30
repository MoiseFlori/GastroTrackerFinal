package com.example.GastroProject.controller;

import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.service.PatientService;
import com.example.GastroProject.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorSymptomController {


    private final SymptomService symptomService;
    private final PatientService patientService;

    @GetMapping("/dr-symptoms/{patientId}")
    public String viewPatientSymptoms(@PathVariable Long patientId, Model model) {
        List<Symptom> patientSymptoms = symptomService.getSymptomsByPatientId(patientId);
        Patient patient = patientService.getPatientById(patientId);
        model.addAttribute("patientSymptoms", patientSymptoms);
        model.addAttribute("patient", patient);
        return "dr-symptoms";
    }

}
