package com.example.GastroProject.configurations;
import com.example.GastroProject.service.dataLoader.DataLoaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.DoctorSchedule;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.DoctorScheduleRepository;
import com.example.GastroProject.service.dataLoader.DataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final DataLoaderService dataLoaderService;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    public AppStartupRunner(DataLoaderService dataLoaderService, DoctorRepository doctorRepository,
                            DoctorScheduleRepository doctorScheduleRepository) {
        this.dataLoaderService = dataLoaderService;
        this.doctorRepository = doctorRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        dataLoaderService.loadData();
        loadDoctorSchedule();
    }

    private void loadDoctorSchedule() {
        List<Doctor> allDoctors = doctorRepository.findAll();

        for (Doctor doctor : allDoctors) {
            for (DayOfWeek day : DayOfWeek.values()) {
                LocalTime startTime, endTime;
                if (day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY) {
                    // Program de la 07:00 la 15:00 pentru luni, miercuri și vineri
                    startTime = LocalTime.of(7, 0);
                    endTime = LocalTime.of(15, 0);
                } else if (day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY) {
                    // Program de la 15:00 la 21:00 pentru marți și joi
                    startTime = LocalTime.of(15, 0);
                    endTime = LocalTime.of(21, 0);
                } else {
                    // Ignoram celelalte zile
                    continue;
                }

                createAndSaveSchedule(doctor, day, startTime, endTime);
            }
        }
    }

    private void createAndSaveSchedule(Doctor doctor, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setDayOfWeek(day);
        doctorSchedule.setStartTime(startTime);
        doctorSchedule.setEndTime(endTime);

        doctorScheduleRepository.save(doctorSchedule);
    }
}
