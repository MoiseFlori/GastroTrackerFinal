package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.DoctorDto;
import com.example.GastroProject.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorDto entityToDTO(Doctor doctor);

    Doctor DTOToEntity(DoctorDto doctorDto);
}
