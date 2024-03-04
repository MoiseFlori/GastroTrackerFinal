package com.example.GastroProject.entity;

import lombok.Getter;

@Getter
public enum Severity {
    MILD("Mild"),
    MODERATE("Moderate"),
    SEVERE("Severe");

    private final String displayName;

    Severity(String displayName) {
        this.displayName = displayName;
    }

}
