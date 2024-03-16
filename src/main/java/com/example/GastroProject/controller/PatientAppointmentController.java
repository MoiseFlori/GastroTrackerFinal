package com.example.GastroProject.controller;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class PatientAppointmentController {


    private final AppointmentService appointmentService;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

        @GetMapping("/home3")
    public String userPage() {
        return "user";
    }


        @GetMapping("/patient-appointments")
    public String showAppointments(Model model,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Patient patient = patientRepository.findByEmail(userEmail);
        List<AppointmentDto> appointments = appointmentService.findByPatientAndKeywordAndDate(patient, keyword,selectedDate);
        model.addAttribute("appointments", Objects.requireNonNullElseGet(appointments, ArrayList::new));
        return "patient-appointments";

    }

    @GetMapping("/add-appointment")
    public String showAddAppointmentForm(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        AppointmentDto appointmentDto = new AppointmentDto();
        model.addAttribute("appointmentDto", appointmentDto);
        model.addAttribute("doctors", doctors);
        return "add-appointment";
    }

    @GetMapping("/available-slots")
    @ResponseBody
    public List<String> getAvailableSlots(@RequestParam Long doctorId, @RequestParam String selectedDate) {
        LocalDate appointmentDate = LocalDate.parse(selectedDate);
        DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(doctorId, dayOfWeek);

        return availableSlots.stream()
                .map(LocalTime::toString)
                .toList();
    }

    @PostMapping("/add-appointment")
    public String submitAppointment(@ModelAttribute AppointmentDto appointmentDto, Principal principal,
                                    @RequestParam Long doctorId) {
        appointmentService.saveAppointmentForPatient(appointmentDto, principal.getName(), doctorId);
        return "redirect:/patient-appointments";
    }


    @GetMapping("/edit-appointment/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        AppointmentDto appointmentDto = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + id));

        model.addAttribute("allDoctors", allDoctors);
        model.addAttribute("selectedDoctors", appointmentDto.getDoctor());
        model.addAttribute("appointment", appointmentDto);

        return "edit-appointment";
    }


    @PostMapping("/edit-appointment/{id}")
    public String updateAppointmentForPatient(@PathVariable Long id, @ModelAttribute("appointment") AppointmentDto updatedAppointment,
                                    @RequestParam Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor Id:" + doctorId));

        updatedAppointment.setId(id);
        updatedAppointment.setDoctor(doctor);

        appointmentService.updateAppointmentForPatient(updatedAppointment);
        return "redirect:/patient-appointments";
    }


    @GetMapping("/delete-appointment/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointmentForPatient(id);
        return "redirect:/patient-appointments";
    }

}
