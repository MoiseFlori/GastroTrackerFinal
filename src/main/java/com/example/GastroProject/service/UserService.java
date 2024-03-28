package com.example.GastroProject.service;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService  extends UserDetailsService {

    String determineRedirectURL(Authentication authentication);

}
