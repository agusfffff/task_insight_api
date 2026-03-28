package com.example.taskanalytics.dto;

import com.example.taskanalytics.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest (
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    String title,
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    String description,
    @NotNull(message = "El estado es requerido")
    TaskStatus status,
    Integer timeSpentMinutes
) {}