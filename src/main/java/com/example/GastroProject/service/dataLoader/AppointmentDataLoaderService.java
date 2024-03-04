package com.example.GastroProject.service.dataLoader;

import com.example.GastroProject.entity.Appointment;
import com.example.GastroProject.entity.User;
import com.example.GastroProject.repository.AppointmentRepository;
import com.example.GastroProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AppointmentDataLoaderService {

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    @Transactional
    public void loadAppointmentFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] appointmentData = line.split(",");

                // Verifică dacă există suficiente elemente în șir și nu sunt goale
                if (appointmentData.length == 5 && Arrays.stream(appointmentData).noneMatch(String::isEmpty)) {
                    Appointment appointment = new Appointment();

                    // Setează appointmentDate cu valoarea din fișier
                    appointment.setAppointmentDate(LocalDate.parse(appointmentData[0]));

                    appointment.setDoctorName(appointmentData[1]);
                    appointment.setSpecialization(appointmentData[2]);

                    // Setează appointmentTime cu valoarea din fișier
                    appointment.setAppointmentTime(LocalTime.parse(appointmentData[3]));

                    User user = userRepository.findByEmail(appointmentData[4]);
                    if (user != null) {
                        appointment.setUser(user);
                    }

                    appointmentRepository.save(appointment);
                }
            }
            System.out.println("Appointments loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        }
    }
}
