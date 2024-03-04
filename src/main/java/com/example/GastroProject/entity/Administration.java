package com.example.GastroProject.entity;

import lombok.Getter;

@Getter
public enum Administration {

    BEFORE_MEAL("Before food"),

    AFTER_MEAL("After food"),

    WITH_MEAL("With food"),

    IRRELEVANT("Doesn't matter");

    private final String displayName;

    Administration(String displayName) {
        this.displayName = displayName;
    }

}
