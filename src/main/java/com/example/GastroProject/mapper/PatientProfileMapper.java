package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.PatientProfileDto;
import com.example.GastroProject.entity.PatientProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientProfileMapper {


    PatientProfileDto entityToDTO(PatientProfile patientProfile);

    PatientProfile DTOToEntity(PatientProfileDto patientProfileDto);
}
