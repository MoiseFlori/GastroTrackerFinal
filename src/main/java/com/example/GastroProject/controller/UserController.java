package com.example.GastroProject.controller;

import com.example.GastroProject.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import com.example.GastroProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Collection;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final DoctorService doctorService;
    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }

    @PostMapping("/redirect")
    public String redirectToRegistrationPage(@RequestParam("role") String role) {
        if ("patient".equals(role)) {
            return "redirect:/registration";
        } else if ("doctor".equals(role)) {
            return "redirect:/registration-doctor";
        } else {
            return "redirect:/error";
        }
    }
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            if (request.getRequestURI().equals("/")) {
                return "welcome";
            }
            return "login";
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isPatient = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_PATIENT"));
        boolean isDoctor = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"));

        if (isPatient) {
            return "redirect:/user-page";
        } else if (isDoctor) {
            return "redirect:/doctor-page";
        } else {
            return "redirect:/welcome";
        }
    }



    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        UserDetails userDetails = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "user";
    }
    @GetMapping("/doctor-page")
    public String doctorPage(Model model, Principal principal) {
        UserDetails userDetails = userService.loadUserByUsername(principal.getName());
        model.addAttribute("doctor", userDetails);
        return "doctor-page";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        UserDetails userDetails = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }


}
