package com.example.GastroProject.exception;

public class TreatmentNotFoundException extends  RuntimeException{
    public TreatmentNotFoundException(String message) {
        super(message);
    }
}
