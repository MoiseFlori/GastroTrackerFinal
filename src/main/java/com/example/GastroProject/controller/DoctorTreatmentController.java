package com.example.GastroProject.controller;

import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.service.PatientService;
import com.example.GastroProject.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorTreatmentController {

    private final TreatmentService treatmentService;
    private final PatientService patientService;


    @GetMapping("/view-treatment/{patientId}")
    public String viewPatientTreatment(@PathVariable Long patientId, Model model,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Patient patient = patientService.getPatientById(patientId);
        List<TreatmentDto> patientTreatments = treatmentService.findByPatientAndKeywordAndDate(patient, keyword, selectedDate);
        model.addAttribute("patientTreatments", Objects.requireNonNullElseGet(patientTreatments, ArrayList::new));
        model.addAttribute("patient", patient);
        return "view-treatment";
    }
    @GetMapping("/add-treatment/{patientId}")
    public String showAddTreatmentForm(@PathVariable Long patientId, Model model) {
        TreatmentDto treatmentDto = new TreatmentDto();
        treatmentDto.setPatientId(patientId);
        model.addAttribute("treatment", treatmentDto);
        return "add-treatment";
    }

    @PostMapping("/add-treatment/{patientId}")
    public String addTreatment(@ModelAttribute("treatment") TreatmentDto treatmentDto, @PathVariable Long patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String doctorEmail = authentication.getName();
        treatmentService.addTreatment(treatmentDto, patientId, doctorEmail);
        return "redirect:/view-treatment/" + patientId;
    }

    @GetMapping("/edit-treatment/{patientId}/{id}")
    public String showEditTreatmentForm(@PathVariable Long patientId, @PathVariable Long id, Model model) {
        model.addAttribute("treatment", treatmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid treatment Id:" + id)));
        return "edit-treatment";
    }


    @PostMapping("/edit-treatment/{patientId}/{id}")
    public String updateTreatment(@PathVariable Long patientId, @PathVariable Long id, @ModelAttribute("treatment") TreatmentDto updatedTreatment) {
        treatmentService.updateTreatment(id, updatedTreatment);
        return "redirect:/view-treatment/" + patientId;
    }


    @GetMapping("/delete-treatment/{patientId}/{id}")
    public String deleteTreatment(@PathVariable Long id,@PathVariable Long patientId) {
        treatmentService.deleteTreatment(id);
        return "redirect:/view-treatment/" + patientId;
    }


}



