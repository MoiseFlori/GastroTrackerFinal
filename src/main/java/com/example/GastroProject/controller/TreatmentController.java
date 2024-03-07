package com.example.GastroProject.controller;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.TreatmentService;
import com.example.GastroProject.util.PdfExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;
    private final UserRepository userRepository;

    @GetMapping("/home2")
    public String userPage() {
        return "user";
    }

    //    @GetMapping("/all-treatments")
//    public String showAllTreatments(Model model) {
//        List<TreatmentDto> treatments =treatmentService.getAllTreatments();
//        model.addAttribute("treatments", treatments);
//        return "all-treatments";
//    }
    @GetMapping("/all-treatments")
    public String showAllTreatments(Model model,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        List<TreatmentDto> treatments = treatmentService.findByUserAndKeywordAndDate(user, keyword, selectedDate);
        model.addAttribute("treatments", Objects.requireNonNullElseGet(treatments, ArrayList::new));
        return "all-treatments";
    }

    @GetMapping("/add-treatment")
    public String showTreatmentForm(Model model) {
        TreatmentDto attributeValue = new TreatmentDto();
        model.addAttribute("treatment", attributeValue);
        return "add-treatment";
    }

    @PostMapping("/add-treatment")
    public String addTreatment(@ModelAttribute("treatment") TreatmentDto treatmentDto, Principal principal) {
        LocalDate datePart = treatmentDto.getLocalDatePart();
        LocalTime timePart = treatmentDto.getLocalTimePart();
        treatmentService.addTreatment(treatmentDto, principal.getName());
        return "redirect:/all-treatments";
    }

    @GetMapping("/edit-treatment/{id}")
    public String showEditTreatmentForm(@PathVariable Long id, Model model) {
        model.addAttribute("treatment", treatmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid treatment Id:" + id)));
        return "edit-treatment";
    }


    @PostMapping("/edit-treatment/{id}")
    public String updateTreatment(@PathVariable Long id, @ModelAttribute("treatment") TreatmentDto updatedTreatment) {
        updatedTreatment.setId(id);
        LocalDate datePart = updatedTreatment.getLocalDatePart();
        LocalTime timePart = updatedTreatment.getLocalTimePart();
        treatmentService.updateTreatment(updatedTreatment);
        return "redirect:/all-treatments";
    }

    @GetMapping("/delete-treatment/{id}")
    public String deleteTreatment(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return "redirect:/all-treatments";
    }

    @GetMapping("/export-pdf")
    public String exportPdf(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User user) {
            List<TreatmentDto> treatmentList = treatmentService.getUserTreatments(user.getId());

            if (treatmentList != null && !treatmentList.isEmpty()) {
                String filePath = "C:\\Users\\Hp\\Downloads\\User_Treatment_PDF.pdf";

                PdfExporter.exportTreatmentListToPdf(treatmentList, filePath);
            }
        }
        return "redirect:/all-treatments";
    }

}
