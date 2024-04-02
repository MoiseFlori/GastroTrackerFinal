# Gastro Tracker

Gastro Tracker is a comprehensive health tracking application designed for both healthcare providers and patients. It allows patients to monitor their symptoms, meals, and documents, as well as schedule appointments with their healthcare providers. Healthcare providers, on the other hand, can prescribe treatments visible to patients, view appointments, add notes, and access patient information including symptoms, documents, and profile details.

## Key Features
- **Two User Roles:**
  - *Patient*: Can input symptoms, log meals, upload documents, schedule appointments, export treatment as PDF, and manage their profile.
  - *Healthcare Provider (Doctor)*: Can view appointments, add notes, prescribe treatments visible to patients, access patient symptoms, documents, and profile details.

- **Authentication and Login:** Users can securely log in to their accounts.

- **Real-time Symptom Tracking:** Patients can record their symptoms to keep track of their health status.

- **Meal Logging:** Patients can log their meals to monitor their dietary habits and nutritional intake.

- **Document Management:** Patients can upload and manage documents related to their health, such as medical reports or prescriptions.

- **Appointment Scheduling:** Patients can schedule appointments with their healthcare providers, and healthcare providers can view and manage these appointments.

- **Treatment Prescriptions:** Healthcare providers can prescribe treatments, which are visible to patients. Patients can export prescribed treatments as PDF.

- **Profile Management:** Users can manage their profiles, including adding information such as allergies.

## Technologies Used
- **Backend:** Spring Boot, Java, Spring Data JPA,Spring Web MVC
- **Frontend:** Spring Thymeleaf, HTML, CSS, JavaScript
- **Database:** MySQL
- **Authentication:** Spring Security,BCrypt
- **Document Generation:** iText PDF (version 5.5.13.3), Apache PDFBox (version 3.0.1)
- **Other Dependencies:** Maven, CSV, commons-io, MapStruct, Lombok


## Installation and Running
To get started with Gastro Tracker, you will need to download the source code from the project repository and open it.

Once you have the source code open in your IDE, you can run the project by executing the main method in the GastroProjectApplication class. The application will then start up, and you can access it in your web browser at http://localhost:8080.

## Usage
Once you've started the application, here's how patients and healthcare providers can use it:

### For Patients:
1. Log in to your account.
2. Input your symptoms, log your meals, upload documents, schedule appointments, and manage your profile.
3. View prescribed treatments, export treatments as PDF, and view upcoming appointments.

### For Doctors:
1. Log in to your account.
2. View appointments scheduled with you by patients.
3. Schedule appointments and prescribe treatments visible to patients.
4. Access patient symptoms, documents, and profile details.



## Contact
If you have any questions, suggestions, or feedback, please contact us at support@gastrotracker.com or through our [website](https://www.gastrotracker.com/contact).

