package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Role;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.RoleRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void saveDoctor(DoctorDto doctorDto) {

        User user = new User();
        user.setName(doctorDto.getName());
        user.setEmail(doctorDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(doctorDto.getPassword()));
        user.setRoles((doctorDto.getRoles()));
        user.getRoles().forEach(role -> {
            final Role roleByName = roleRepository.findByName(role.getName());
            role.setId(Objects.requireNonNullElseGet(roleByName, () -> roleRepository.save(role)).getId());
        });
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setStamp(doctorDto.getStamp());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setUser(user);
        doctorRepository.save(doctor);
    }


    }

