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

@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorAppointmentController {

private final AppointmentService appointmentService;

private final DoctorRepository doctorRepository;

private final PatientRepository patientRepository;

    @GetMapping("/home5")
    public String doctorPage() {
        return "doctor-page";
    }


    @GetMapping("/doctor-appointments")
    public String showAppointments(Model model,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Doctor doctor = doctorRepository.findByEmail(userEmail);
        List<AppointmentDto> appointments = appointmentService.findByDoctorAndKeywordAndDate(doctor, keyword,selectedDate);
        model.addAttribute("appointments", Objects.requireNonNullElseGet(appointments, ArrayList::new));
        return "doctor-appointments";

    }

    @GetMapping("/add-drAppointment")
    public String showAddAppointmentForm(Model model, Principal principal) {
        Doctor doctor = doctorRepository.findByEmail(principal.getName());
        List<Patient> patients = doctor.getPatients();
        Long doctorId = doctor.getDoctorId();
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setDoctorId(doctorId);
        model.addAttribute("appointmentDto", appointmentDto);
        model.addAttribute("patients", patients);
        return "add-drAppointment";
    }

    @GetMapping("/available")
    @ResponseBody
    public List<String> getAvailableSlots(@RequestParam Long doctorId, @RequestParam String selectedDate) {
        LocalDate appointmentDate = LocalDate.parse(selectedDate);
        DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(doctorId, dayOfWeek);

        return availableSlots.stream()
                .map(LocalTime::toString)
                .toList();
    }

    @PostMapping("/add-drAppointment")
    public String submitAppointment(@ModelAttribute AppointmentDto appointmentDto, Principal principal,
                                    @RequestParam Long patientId) {
        appointmentService.saveAppointmentForDoctor(appointmentDto, principal.getName(), patientId);
        return "redirect:/doctor-appointments";
    }

    @GetMapping("/edit-drAppointment/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model) {
        AppointmentDto appointmentDto = appointmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + id));

        Doctor doctor = appointmentDto.getDoctor();
        Long doctorId = doctor.getDoctorId();
        List<Patient> doctorPatients = doctor.getPatients();

        appointmentDto.setDoctorId(doctorId);

        model.addAttribute("allPatients", doctorPatients);
        model.addAttribute("selectedPatient", appointmentDto.getPatient());
        model.addAttribute("appointment", appointmentDto);

        return "edit-drAppointment";
    }



    @PostMapping("/edit-drAppointment/{id}")
    public String updateAppointmentForPatient(@PathVariable Long id, @ModelAttribute("appointment") AppointmentDto updatedAppointment,
                                              @RequestParam Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + patientId));

        updatedAppointment.setId(id);
        updatedAppointment.setPatient(patient);

        appointmentService.updateAppointmentForDoctor(updatedAppointment);
        return "redirect:/doctor-appointments";
    }

    @GetMapping("/delete-drAppointment/{id}")
    public String deleteAppointmentForDoctor(@PathVariable Long id) {
        appointmentService.deleteAppointmentForDoctor(id);
        return "redirect:/doctor-appointments";
    }


}
