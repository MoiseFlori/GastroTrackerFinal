package com.example.GastroProject.service.impl;

import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.*;
import com.example.GastroProject.exception.TreatmentNotFoundException;
import com.example.GastroProject.mapper.TreatmentMapper;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import com.example.GastroProject.repository.TreatmentRepository;
import com.example.GastroProject.service.TreatmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final PatientRepository patientRepository;

    private final TreatmentRepository treatmentRepository;

    private final TreatmentMapper treatmentMapper;

    private final DoctorRepository doctorRepository;


    public void addTreatment(TreatmentDto treatmentDto, Long patientId, String doctorEmail) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            Doctor doctor = doctorRepository.findByEmail(doctorEmail);

            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with email: " + doctorEmail);
            }

            Treatment treatment = treatmentMapper.DTOToEntity(treatmentDto);
            treatment.setPatient(patient);
            treatment.setDoctor(doctor);

            patient.addTreatment(treatment);

            patientRepository.save(patient);
        } else {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }
    }


    @Override
    public Optional<TreatmentDto> findById(Long id) {
        var optionalTreatment = treatmentRepository.findById(id);
        return optionalTreatment.map(treatmentMapper::entityToDTO);
    }

    @Override
    public void updateTreatment(Long id, TreatmentDto updatedTreatment) {
        Optional<Treatment> optionalTreatment = treatmentRepository.findById(id);

        if (optionalTreatment.isPresent()) {
            Treatment existingTreatment = optionalTreatment.get();
            existingTreatment.setName(updatedTreatment.getName());
            existingTreatment.setDose(updatedTreatment.getDose());
            existingTreatment.setMedicineType(updatedTreatment.getMedicineType());
            existingTreatment.setAdministration(updatedTreatment.getAdministration());
            existingTreatment.setDescription(updatedTreatment.getDescription());
            existingTreatment.setDurationInDays(updatedTreatment.getDurationInDays());
            existingTreatment.setStartTreatment(updatedTreatment.getStartTreatment());
            existingTreatment.setEndTreatment(updatedTreatment.getStartTreatment().plusDays(updatedTreatment.getDurationInDays()).minusDays(1));

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
    public List<TreatmentDto> findByPatientAndKeywordAndDate(Patient patient, String keyword, LocalDate selectedDate) {
        List<Treatment> treatments = treatmentRepository.findByPatient(patient, Sort.by(Sort.Direction.DESC, "startTreatment"));
        return treatments.stream()
                .filter(treatment ->
                        (selectedDate == null || treatment.getStartTreatment().equals(selectedDate) ||
                                (treatment.getEndTreatment() != null && treatment.getEndTreatment().equals(selectedDate))) &&
                                (keyword == null ||
                                        treatment.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                        treatment.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                                        treatment.getAdministration().name().toLowerCase().contains(keyword.toLowerCase()) ||
                                        treatment.getMedicineType().name().toLowerCase().contains(keyword.toLowerCase()) ||
                                        treatment.getDose().toLowerCase().contains(keyword.toLowerCase()) ||
                                        treatment.getDurationInDays().toString().contains(keyword)))
                .map(treatmentMapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<TreatmentDto> getPatientTreatments(String email) {
        Patient patient = patientRepository.findByEmail(email);

        if (patient != null) {
            return patient.getTreatments().stream()
                    .map(treatmentMapper::entityToDTO)
                    .toList();
        } else {
            throw new EntityNotFoundException("Patient with email " + email + " not found");
        }
    }


}






