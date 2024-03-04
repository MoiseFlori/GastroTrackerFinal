package com.example.GastroProject.mapper;

import com.example.GastroProject.dto.UserDto;
import com.example.GastroProject.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto entityToDTO(User user);

    User DTOToEntity(UserDto userDto);
}