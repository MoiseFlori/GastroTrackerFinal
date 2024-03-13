package com.example.GastroProject.service;

import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface TreatmentService {
    List<TreatmentDto> getAllTreatments();

    void addTreatment(TreatmentDto treatmentDto, String email);

    Optional<TreatmentDto> findById(Long id);

    void updateTreatment(TreatmentDto updatedTreatment);

    void deleteTreatment(Long id);

    List<TreatmentDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate);

    List<TreatmentDto> getPatientTreatments(Long userId);
}
