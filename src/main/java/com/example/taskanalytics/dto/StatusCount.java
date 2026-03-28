package com.example.taskanalytics.dto;

import com.example.taskanalytics.model.TaskStatus;

public record StatusCount(TaskStatus status, long count) {}