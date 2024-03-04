package com.example.GastroProject.controller;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.UserProfileDto;
import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.entity.UserProfile;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.UIResource;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AppointmentController {

private final AppointmentService appointmentService;
private final UserRepository userRepository;

    @GetMapping("/home3")
    public String userPage() {
        return "user";
    }
    @GetMapping("/appointments")
    public String showAppointments(Model model,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        List<AppointmentDto> appointments = appointmentService.findByUserAndKeywordAndDate(user, keyword,selectedDate);
        model.addAttribute("appointments", Objects.requireNonNullElseGet(appointments, ArrayList::new));
        return "appointments";

    }



    @GetMapping("/add-appointment")
    public String showAddAppointmentForm(Model model) {
        AppointmentDto attributeValue = new AppointmentDto();
        model.addAttribute("appointment", attributeValue);
        return "add-appointment";
    }
    @PostMapping("/add-appointment")
    public String submitAppointment(@ModelAttribute AppointmentDto appointmentDto, Principal principal) {
        appointmentService.saveAppointment(appointmentDto,principal.getName());
        return "redirect:/appointments";
    }


    @GetMapping("/edit-appointment/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model) {
        AppointmentDto appointmentDto = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + id));
        model.addAttribute("appointment", appointmentDto);

        return "edit-appointment";
    }


    @PostMapping("/edit-appointment/{id}")
    public String updateAppointment(@PathVariable Long id, @ModelAttribute("appointment") AppointmentDto updatedAppointment) {
        updatedAppointment.setId(id);
        LocalDate datePart = updatedAppointment.getAppointmentDate();
        LocalTime timePart = updatedAppointment.getAppointmentTime();
        appointmentService.updateAppointment(updatedAppointment);
        return "redirect:/appointments";
    }


    @GetMapping("/delete-appointment/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }

}
