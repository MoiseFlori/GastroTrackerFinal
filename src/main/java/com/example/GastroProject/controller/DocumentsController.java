package com.example.GastroProject.controller;

import com.example.GastroProject.entity.Document;
import com.example.GastroProject.entity.Patient;
import com.example.GastroProject.repository.DocumentRepository;
import com.example.GastroProject.repository.PatientRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class DocumentsController {

    private final DocumentRepository documentRepository;

    private final PatientRepository patientRepository;

    @GetMapping("/home6")
    public String userPage() {
        return "user";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("patientFileName") String patientFileName, Principal principal) {
        if (file.isEmpty()) {
            return "redirect:/uploadFailure";
        }

        try {
            String email = principal.getName();
            Patient patient = patientRepository.findByEmail(email);

            // Directorul unde se vor salva fișierele
            String uploadDir = "C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA FILE";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Calea fisierului
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String filePath = uploadDir + File.separator + patientFileName + "." + fileExtension;
            File dest = new File(filePath);
            file.transferTo(dest);

            Document document = new Document();
            document.setFileName(patientFileName);
            document.setFilePath(filePath);
            document.setPatient(patient);
            documentRepository.save(document);

            return "redirect:/documents";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/uploadFailure";
        }
    }

    @GetMapping("/uploadFailure")
    public String uploadFailure() {
        return "uploadFailure";
    }
    @GetMapping("/documents")
    public String showDocuments(Model model, Principal principal) {
        String email = principal.getName();
        Patient patient = patientRepository.findByEmail(email);
        List<Document> documents = documentRepository.findByPatient(patient);
        model.addAttribute("documents", documents);
        return "documents";
    }

    @GetMapping("/loadDocument/{id}")
    public void loadDocument(@PathVariable Long id, HttpServletResponse response) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();

            // seteaza tipul de conținut al raspunsului HTTP corespunzator
            String contentType;
            try {
                contentType = Files.probeContentType(Paths.get(document.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType(contentType);

            // incarca continutul fisierului si il  transmite direct ca raspuns HTTP
            try (InputStream inputStream = new FileInputStream(new File(document.getFilePath()))) {
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


}
