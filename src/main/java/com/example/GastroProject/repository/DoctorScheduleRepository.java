package com.example.GastroProject.repository;

import com.example.GastroProject.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;


public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek")
    DoctorSchedule findByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}

