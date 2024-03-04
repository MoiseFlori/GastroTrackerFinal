package com.example.GastroProject.dto;


import com.example.GastroProject.entity.Role;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private Set<Role> roles;

    private String name;

    private String email;

    private String password;

    private List<SymptomDto> symptoms;

}
