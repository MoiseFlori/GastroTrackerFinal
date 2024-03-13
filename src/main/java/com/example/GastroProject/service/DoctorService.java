package com.example.GastroProject.service;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {

    void saveDoctor(DoctorDto doctorDto);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long doctorId);

    List<Patient> getDoctorPatients(Long doctorId);

    Doctor getDoctorByEmail(String email);

    void editPatient(Long id, PatientDto editedPatientDto);

    void dischargePatient(Long doctorId, Long patientId);


   List<Patient> searchDoctorPatientsByKeyword(Long doctorId, String keyword);


}
