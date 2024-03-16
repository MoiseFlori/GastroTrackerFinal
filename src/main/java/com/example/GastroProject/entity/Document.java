package com.example.GastroProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String filePath;

    @ManyToOne(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.MERGE})
    private Patient patient;

    public boolean isImage() {
        // Verifică extensia fișierului
        String extension = this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png");
    }

}
