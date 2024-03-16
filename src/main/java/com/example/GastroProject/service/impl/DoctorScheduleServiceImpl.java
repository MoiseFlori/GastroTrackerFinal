package com.example.GastroProject.service.impl;

import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.DoctorSchedule;
import com.example.GastroProject.entity.SchedulePeriod;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.DoctorScheduleRepository;
import com.example.GastroProject.service.AppointmentService;
import com.example.GastroProject.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {


}




