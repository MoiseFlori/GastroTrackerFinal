package com.example.GastroProject.service;



import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService  extends UserDetailsService {

	User save(UserDto userDto);


}
