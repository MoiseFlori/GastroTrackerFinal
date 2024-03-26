package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.TreatmentDto;
import com.example.GastroProject.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    TreatmentDto entityToDTO(Treatment treatment);
    @Mapping(target = "startTreatment", source = "startTreatment")
    @Mapping(target = "endTreatment", source = "endTreatment")
    Treatment DTOToEntity(TreatmentDto treatmentDto);
}
