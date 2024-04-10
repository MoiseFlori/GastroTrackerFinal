package com.example.GastroProject.controller;

import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.service.TreatmentService;
import com.example.GastroProject.util.PdfExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class PatientTreatmentController {
    private final TreatmentService treatmentService;
    private final PatientRepository patientRepository;

    @GetMapping("/home2")
    public String userPage() {
        return "user";
    }

    @GetMapping("/all-treatments")
    public String showAllTreatments(Model model,
                                              @RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Patient patient = patientRepository.findByEmail(userEmail);
        List<TreatmentDto> treatments = treatmentService.findByPatientAndKeywordAndDate(patient, keyword, selectedDate);
        model.addAttribute("treatments", Objects.requireNonNullElseGet(treatments, ArrayList::new));
        return "all-treatments";
    }



    @GetMapping("/treatment-details/{id}")
    public String showTreatmentDetailsForPatient(@PathVariable Long id, Model model) {
        Optional<TreatmentDto> optionalTreatment = treatmentService.findById(id);

        if (optionalTreatment.isPresent()) {
            TreatmentDto treatment = optionalTreatment.get();

            LocalDate currentDate = LocalDate.now();
            long daysRemaining = ChronoUnit.DAYS.between(currentDate, treatment.getEndTreatment());

            model.addAttribute("treatment", treatment);
            model.addAttribute("daysRemaining", daysRemaining);

            if (daysRemaining <= 0) {
                model.addAttribute("isFinished", true);
            } else {
                model.addAttribute("isFinished", false);
            }

        }
        return "treatmentDetails";
    }


    @PostMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPatientTreatmentToPdf() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String patientName = authentication.getName();
            List<TreatmentDto> patientTreatments = treatmentService.getPatientTreatments(patientName);

            if (!patientTreatments.isEmpty()) {
                byte[] pdfBytes = PdfExporter.exportTreatmentListToPdf(patientName, patientTreatments);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("filename", "treatments.pdf");
                headers.setContentLength(pdfBytes.length);

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no treatments for you".getBytes());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("An error occurred while exporting to PDF: " + e.getMessage()).getBytes());
        }
    }


}
