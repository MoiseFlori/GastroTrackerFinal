package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.entity.Symptom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SymptomMapper {

    SymptomDto entityToDTO(Symptom symptom);

    @Mapping(target = "localDatePart", source = "localDatePart")
    @Mapping(target = "localTimePart", source = "localTimePart")
    Symptom DTOToEntity(SymptomDto symptomDto);


}
