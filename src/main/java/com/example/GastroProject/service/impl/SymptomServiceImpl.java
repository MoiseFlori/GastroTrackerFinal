package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.exception.SymptomNotFoundException;
import com.example.GastroProject.mapper.SymptomMapper;
import com.example.GastroProject.repository.SymptomRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.SymptomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SymptomServiceImpl implements SymptomService {

    private final UserRepository userRepository;

    private final SymptomRepository symptomRepository;

    private final SymptomMapper symptomMapper;

    public void updateSymptom(SymptomDto updatedSymptomDto) {
        Optional<Symptom> optionalSymptom = symptomRepository.findById(updatedSymptomDto.getId());

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



//    @Override
//    public List<SymptomDto> findByKeyword(String keyword) {
//        if (keyword == null) {
//            return symptomRepository.findAll(Sort.by(Sort.Direction.ASC, "localDatePart")).stream()
//                    .map(symptomMapper::entityToDTO)
//                    .toList();
//
//        }
//        List<Symptom> symptoms = symptomRepository.findByKeyword(keyword, Sort.by(Sort.Direction.ASC, "localDatePart"));
//        return symptoms.stream()
//                .map(symptomMapper::entityToDTO)
//                .toList();
//
//    }

    @Override
    public List<SymptomDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate) {
        List<Symptom> symptoms = symptomRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "localDatePart"));
        return symptoms.stream()
                .filter(symptom -> (selectedDate == null || symptom.getLocalDatePart().equals(selectedDate)) &&
                        (keyword == null ||
                                symptom.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                symptom.getSeverity().name().toLowerCase().contains(keyword.toLowerCase()) ||
                                symptom.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .map(symptomMapper::entityToDTO)
                .toList();
    }



//    public List<SymptomDto> getAllSymptoms() {
//        List<Symptom> symptoms = symptomRepository.findAll(Sort.by(Sort.Direction.ASC, "localDatePart"));
//        return symptoms.stream()
//                .map(symptomMapper::entityToDTO)
//                .toList();
//    }


    public void deleteSymptom(Long id) {
        symptomRepository.deleteById(id);
    }


    public void addSymptom(SymptomDto symptomDto, String email) {
        User user = userRepository.findByEmail(email);
        Symptom symptom = symptomMapper.DTOToEntity(symptomDto);
        user.addSymptom(symptom);
        userRepository.save(user);
    }


}
