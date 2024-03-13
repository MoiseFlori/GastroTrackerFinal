package com.example.GastroProject.service;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface SymptomService {


    void addSymptom(SymptomDto symptomDto, String email);


    //List<SymptomDto> getAllSymptoms();

    void deleteSymptom(Long id);

    void updateSymptom(SymptomDto updatedSymptomDto);


    Optional<SymptomDto> findById(Long id);


    //List<SymptomDto> findByKeyword(String keyword);

    List<SymptomDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate);
}
