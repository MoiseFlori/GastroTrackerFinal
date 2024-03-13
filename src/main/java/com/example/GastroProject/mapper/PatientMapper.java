package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.PatientDto;
import com.example.GastroProject.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto entityToDTO(Patient patient);

    Patient DTOToEntity(PatientDto patientDto);
}
