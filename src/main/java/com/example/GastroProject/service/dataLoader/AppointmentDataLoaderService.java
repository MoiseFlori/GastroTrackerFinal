package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.Doctor;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.repository.AppointmentRepository;
import com.example.GastroProject.repository.DoctorRepository;
import com.example.GastroProject.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AppointmentDataLoaderService {

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;
    @Transactional
    public void loadAppointmentsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] appointmentData = line.split(",");
                Appointment appointment = new Appointment();
                appointment.setAppointmentDate(LocalDate.parse(appointmentData[0]));
                appointment.setAppointmentTime(LocalTime.parse(appointmentData[1]));

                Long doctorId = Long.parseLong(appointmentData[2]);
                Long patientId = Long.parseLong(appointmentData[3]);

                Doctor doctor = doctorRepository.findById(doctorId)
                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

                Patient patient = patientRepository.findById(patientId)
                        .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

                appointment.setDoctor(doctor);
                appointment.setPatient(patient);

                appointmentRepository.save(appointment);
            }
            System.out.println("Appointments loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }


}
