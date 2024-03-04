package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Meal;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.Treatment;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.exception.MealNotFoundException;
import com.example.GastroProject.exception.TreatmentNotFoundException;
import com.example.GastroProject.mapper.SymptomMapper;
import com.example.GastroProject.mapper.TreatmentMapper;
import com.example.GastroProject.repository.SymptomRepository;
import com.example.GastroProject.repository.TreatmentRepository;
import com.example.GastroProject.repository.UserRepository;
import com.example.GastroProject.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final UserRepository userRepository;

    private final TreatmentRepository treatmentRepository;

    private final TreatmentMapper treatmentMapper;


        @Override
        public List<TreatmentDto> getAllTreatments() {
            List<Treatment> treatments = treatmentRepository.findAll();
            return treatments.stream()
                    .map(treatmentMapper::entityToDTO)
                    .toList();
        }

    @Override
    public void addTreatment(TreatmentDto treatmentDto, String email) {
        User user = userRepository.findByEmail(email);
        Treatment treatment = treatmentMapper.DTOToEntity(treatmentDto);
        user.addTreatment(treatment);
        userRepository.save(user);
    }

    @Override
    public Optional<TreatmentDto> findById(Long id) {
        var optionalTreatment = treatmentRepository.findById(id);
        return optionalTreatment.map(treatmentMapper::entityToDTO);
    }

    @Override
    public void updateTreatment(TreatmentDto updatedTreatment) {
        Optional<Treatment> optionalTreatment = treatmentRepository.findById(updatedTreatment.getId());

        if (optionalTreatment.isPresent()) {
            Treatment existingTreatment = optionalTreatment.get();
            existingTreatment.setLocalDatePart(updatedTreatment.getLocalDatePart());
            existingTreatment.setLocalTimePart(updatedTreatment.getLocalTimePart());
            existingTreatment.setName(updatedTreatment.getName());
            existingTreatment.setDose(updatedTreatment.getDose());
            existingTreatment.setMedicineType(updatedTreatment.getMedicineType());
            existingTreatment.setAdministration(updatedTreatment.getAdministration());
            existingTreatment.setDescription(updatedTreatment.getDescription());

            treatmentRepository.save(existingTreatment);
        } else {
            throw new TreatmentNotFoundException("Treatment with ID " + updatedTreatment.getId() + " not found");
        }
    }

    @Override
    public void deleteTreatment(Long id) {
            treatmentRepository.deleteById(id);

    }

    @Override
    public List<TreatmentDto> findByKeyword(String keyword) {
        if(keyword == null){
            return treatmentRepository.findAll(Sort.by(Sort.Direction.ASC, "localDatePart")).stream()
                    .map(treatmentMapper::entityToDTO)
                    .toList();

        }
        List<Treatment> treatments = treatmentRepository.findByKeyword(keyword,Sort.by(Sort.Direction.ASC, "localDatePart"));
        return treatments.stream()
                .map(treatmentMapper::entityToDTO)
                .toList();

    }

    @Override
    public List<TreatmentDto> findByUserAndKeywordAndDate(User user, String keyword, LocalDate selectedDate) {
        List<Treatment> treatments = treatmentRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "localDatePart"));
        return treatments.stream()
                .filter(treatment ->(selectedDate == null || treatment.getLocalDatePart().equals(selectedDate)) &&
                        (keyword == null || // Verifică dacă keyword este null
                        treatment.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        treatment.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        treatment.getAdministration().name().toLowerCase().contains(keyword.toLowerCase()) ||
                        treatment.getMedicineType().name().toLowerCase().contains(keyword.toLowerCase()) ||
                        treatment.getDose().toLowerCase().contains(keyword.toLowerCase())))
                .map(treatmentMapper::entityToDTO)
                .toList();
    }
    }






