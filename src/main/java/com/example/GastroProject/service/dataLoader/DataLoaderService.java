package com.example.GastroProject.service.dataLoader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final SymptomDataLoaderService symptomDataLoaderService;
    private final MealDataLoaderService mealDataLoaderService;
    private final TreatmentDataLoaderService treatmentDataLoaderService;
    private final PatientProfileDataLoaderService patientProfileDataLoaderService;
    private final UserRegistrationDataLoaderService userRegistrationDataLoaderService;
    private final RoleDataLoaderService roleDataLoaderService;
    private final AppointmentDataLoaderService appointmentDataLoaderService;
    private final PatientDataLoaderService patientDataLoaderService;
    private final DoctorDataLoaderService doctorDataLoaderService;


    public void loadData() {
        roleDataLoaderService.loadRolesFromFile("src/main/resources/roleCSV.csv");
        userRegistrationDataLoaderService.loadUserDataFromFile("src/main/resources/userRegistrationCSV.csv");
        doctorDataLoaderService.loadDoctorDataFromFile("src/main/resources/doctorCSV.csv");
        patientDataLoaderService.loadPatientDataFromFile( "src/main/resources/patientCSV.csv");
       symptomDataLoaderService.loadSymptomsFromFile("src/main/resources/symptomCSV.csv");
        mealDataLoaderService.loadMealsFromFile("src/main/resources/mealCSV.csv");
        treatmentDataLoaderService.loadTreatmentsFromFile("src/main/resources/treatmentCSV.csv");
        patientProfileDataLoaderService.loadUserProfilesFromFile("src/main/resources/patientProfileCSV.csv");
        //appointmentDataLoaderService.loadAppointmentsFromFile("src/main/resources/appointmentCSV.csv");

    }
}
