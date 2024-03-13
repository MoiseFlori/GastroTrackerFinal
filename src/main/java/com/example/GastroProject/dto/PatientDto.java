package com.example.GastroProject.dto;

import com.example.GastroProject.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto extends UserDto{


    private Integer age;

    private String diagnosis;

    private Doctor doctor;

    private Long doctorId;

}
