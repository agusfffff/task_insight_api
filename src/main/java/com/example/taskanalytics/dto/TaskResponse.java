package com.example.taskanalytics.dto;

import com.example.taskanalytics.model.TaskStatus;
import java.time.LocalDateTime;

public record TaskResponse (
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    Integer timeSpentMinutes
) {}