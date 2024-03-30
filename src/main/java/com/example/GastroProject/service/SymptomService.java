package com.example.GastroProject.service;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface SymptomService {


    void addSymptom(SymptomDto symptomDto, String email);


    void deleteSymptom(Long id);

    void updateSymptom(Long id,SymptomDto updatedSymptomDto);


    Optional<SymptomDto> findById(Long id);


    List<SymptomDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate);

    List<Symptom> getSymptomsByPatientId(Long patientId);
}
