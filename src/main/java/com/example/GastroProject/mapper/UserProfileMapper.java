package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.dto.UserProfileDto;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {


    UserProfileDto entityToDTO(UserProfile userProfile);

    UserProfile DTOToEntity(UserProfileDto userProfileDto);
}
