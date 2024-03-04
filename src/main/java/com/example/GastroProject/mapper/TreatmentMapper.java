package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    TreatmentDto entityToDTO(Treatment treatment);
    @Mapping(target = "localDatePart", source = "localDatePart") //qualifiedByName = "localDateTimeToString")
    @Mapping(target = "localTimePart", source = "localTimePart")
    Treatment DTOToEntity(TreatmentDto treatmentDto);
}
