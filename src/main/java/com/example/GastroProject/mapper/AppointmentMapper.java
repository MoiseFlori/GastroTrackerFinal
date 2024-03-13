package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.AppointmentDto;
import com.example.GastroProject.dto.MealDto;
import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDto entityToDTO(Appointment appointment);
    Appointment DTOToEntity(AppointmentDto appointmentDto);
}
