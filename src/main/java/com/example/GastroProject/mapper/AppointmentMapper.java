package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDto entityToDTO(Appointment appointment);
    Appointment DTOToEntity(AppointmentDto appointmentDto);
}
