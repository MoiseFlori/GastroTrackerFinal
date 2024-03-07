package com.example.GastroProject.service;

import com.example.GastroProject.dto.DoctorDto;
import org.springframework.stereotype.Service;

@Service
public interface DoctorService {

    void saveDoctor(DoctorDto doctorDto);
}
