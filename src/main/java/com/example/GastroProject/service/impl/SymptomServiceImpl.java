package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.SymptomMapper;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.SymptomRepository;
import com.example.GastroProject.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {

    private final PatientRepository patientRepository;

    private final SymptomRepository symptomRepository;

    private final SymptomMapper symptomMapper;

    public void updateSymptom(Long id,SymptomDto updatedSymptomDto) {
        Optional<Symptom> optionalSymptom = symptomRepository.findById(id);

        if (optionalSymptom.isPresent()) {
            Symptom existingSymptom = optionalSymptom.get();

            existingSymptom.setLocalDatePart(updatedSymptomDto.getLocalDatePart());
            existingSymptom.setLocalTimePart(updatedSymptomDto.getLocalTimePart());
            existingSymptom.setName(updatedSymptomDto.getName());
            existingSymptom.setSeverity(updatedSymptomDto.getSeverity());
            existingSymptom.setDescription(updatedSymptomDto.getDescription());

            symptomRepository.save(existingSymptom);
        } else {
            throw new SymptomNotFoundException("Symptom with ID " + updatedSymptomDto.getId() + " not found");
        }
    }

    @Override
    public Optional<SymptomDto> findById(Long id) {
        var optionalSymptom = symptomRepository.findById(id);
        return optionalSymptom.map(symptomMapper::entityToDTO);
    }



    @Override
    public List<SymptomDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate) {
        List<Symptom> symptoms = symptomRepository.findByPatient(patient, Sort.by(Sort.Direction.DESC, "localDatePart"));
        return symptoms.stream()
                .filter(symptom -> (selectedDate == null || symptom.getLocalDatePart().equals(selectedDate)) &&
                        (keyword == null ||
                                symptom.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                symptom.getSeverity().name().toLowerCase().contains(keyword.toLowerCase()) ||
                                symptom.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .map(symptomMapper::entityToDTO)
                .toList();
    }

    @Override
    public List<Symptom> getSymptomsByPatientId(Long patientId) {
        return symptomRepository.findByPatientId(patientId);
    }


    public void deleteSymptom(Long id) {
        symptomRepository.deleteById(id);
    }


    public void addSymptom(SymptomDto symptomDto, String email) {
        Patient patient = patientRepository.findByEmail(email);
        Symptom symptom = symptomMapper.DTOToEntity(symptomDto);
        patient.addSymptom(symptom);
        patientRepository.save(patient);
    }


}
