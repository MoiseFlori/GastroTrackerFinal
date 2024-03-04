package com.example.GastroProject.service.dataLoader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final SymptomDataLoaderService symptomDataLoaderService;
    private final MealDataLoaderService mealDataLoaderService;
    private final TreatmentDataLoaderService treatmentDataLoaderService;
    private final UserProfileDataLoaderService userProfileDataLoaderService;
    private final UserRegistrationDataLoaderService userRegistrationDataLoaderService;
    private final RoleDataLoaderService roleDataLoaderService;
    private final AppointmentDataLoaderService appointmentDataLoaderService;

    public void loadData() {
        roleDataLoaderService.loadRolesFromFile("src/main/resources/roleCSV.csv");
        userRegistrationDataLoaderService.loadUserDataFromFile("src/main/resources/userRegistrationCSV.csv");
        symptomDataLoaderService.loadSymptomsFromFile("src/main/resources/symptomCSV.csv");
        mealDataLoaderService.loadMealsFromFile("src/main/resources/mealCSV.csv");
        treatmentDataLoaderService.loadTreatmentsFromFile("src/main/resources/treatmentCSV.csv");
        userProfileDataLoaderService.loadUserProfilesFromFile("src/main/resources/userprofileCSV.csv");
        appointmentDataLoaderService.loadAppointmentFromFile("src/main/resources/appointmentCSV.csv");
    }
}
