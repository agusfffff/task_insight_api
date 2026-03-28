package com.example.taskanalytics.model;

public enum TaskStatus {
    TODO("Por hacer"),
    IN_PROGRESS("En progreso"),
    DONE("Completada");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}