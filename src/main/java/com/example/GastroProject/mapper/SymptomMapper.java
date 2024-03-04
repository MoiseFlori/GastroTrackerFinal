package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.SymptomDto;
import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface SymptomMapper {

    SymptomDto entityToDTO(Symptom symptom);

    @Mapping(target = "localDatePart", source = "localDatePart") //qualifiedByName = "localDateTimeToString")
    @Mapping(target = "localTimePart", source = "localTimePart") //qualifiedByName = "localDateTimeToString")
    Symptom DTOToEntity(SymptomDto symptomDto);

//    @Named("localDateTimeToString")
//    default LocalDateTime mapLocalDateTimeToString(LocalDateTime localDateTime) {
//        return localDateTime;
//    }
}
