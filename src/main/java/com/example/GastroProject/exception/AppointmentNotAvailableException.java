package com.example.GastroProject.exception;

public class AppointmentNotAvailableException extends RuntimeException{
    public AppointmentNotAvailableException(String message) {
        super(message);
    }
}
