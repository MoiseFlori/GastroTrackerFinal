package com.example.GastroProject.controller;

import com.example.GastroProject.entity.Document;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.entity.Symptom;
import com.example.GastroProject.repository.DocumentRepository;
import com.example.GastroProject.service.DocumentService;
import com.example.GastroProject.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class DoctorDocumentsController {


    private final DocumentService documentService;
    private final PatientService patientService;

    @GetMapping("/dr-documents/{patientId}")
    public String getPatientDocuments(@PathVariable Long patientId, Model model) {
        List<Document> patientDocuments = documentService.getDocumentsByPatientId(patientId);
        Patient patient = patientService.getPatientById(patientId);
        model.addAttribute("patientDocuments", patientDocuments);
        model.addAttribute("patient", patient);
        return "dr-documents";
    }


    }


