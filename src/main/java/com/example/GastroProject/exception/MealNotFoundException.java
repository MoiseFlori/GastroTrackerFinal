package com.example.GastroProject.exception;

public class MealNotFoundException extends RuntimeException{
    public MealNotFoundException(String message) {
        super(message);
    }
}
