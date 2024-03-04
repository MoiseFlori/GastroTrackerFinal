package com.example.GastroProject.controller;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import com.example.GastroProject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

//    @GetMapping("/user-profile")
//    public String userProfile() {
//        return "user-profile";
//    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        userDto.setRoles(Collections.singleton(new Role(Constants.ROLE_USER)));
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "redirect:/login";
    }

    @RequestMapping(value = {"/login", "/"})
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/user-page";
    }


    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        UserDetails userDetails = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "user";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        UserDetails userDetails = userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }


}
