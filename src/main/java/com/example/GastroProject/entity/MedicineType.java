package com.example.GastroProject.entity;

import lombok.Getter;

@Getter
public enum MedicineType {
    CAPSULE("Capsule"),
    INJECTION("Injection"),
    PROCEDURES("Procedures"),
    DROPS("Drops"),
    LIQUID("Liquid"),
    CREAM("Unguent/Cream"),
    SPRAY("Spray");


    private final String displayName;

    MedicineType(String displayName) {
        this.displayName = displayName;
    }

}
