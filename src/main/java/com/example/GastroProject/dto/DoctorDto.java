package com.example.GastroProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto extends UserDto  {


    private String stamp;

    private String specialization;

}
