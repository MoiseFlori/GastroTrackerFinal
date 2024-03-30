package com.example.GastroProject.service;

import com.example.GastroProject.entity.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {

    List<Document> getDocumentsByPatientId(Long patientId);

}
