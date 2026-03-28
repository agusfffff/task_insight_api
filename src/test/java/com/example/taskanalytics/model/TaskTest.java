package com.example.taskanalytics.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        Task task = new Task();

        LocalDateTime now = LocalDateTime.now();

        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus(TaskStatus.TODO);
        task.setCompletedAt(now);
        task.setTimeSpentMinutes(30);

        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(now, task.getCompletedAt());
        assertEquals(30, task.getTimeSpentMinutes());
    }

    @Test
    void shouldFailWhenTitleIsBlank() {
        Task task = new Task();
        task.setTitle(""); 
        task.setStatus(TaskStatus.TODO);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenStatusIsNull() {
        Task task = new Task();
        task.setTitle("Valid title");

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTimeSpentIsNegative() {
        Task task = new Task();
        task.setTitle("Valid title");
        task.setStatus(TaskStatus.TODO);
        task.setTimeSpentMinutes(-10); 

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenTaskIsValid() {
        Task task = new Task();
        task.setTitle("Valid title");
        task.setStatus(TaskStatus.TODO);
        task.setTimeSpentMinutes(15);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsOnlySpaces() {
        Task task = new Task();
        task.setTitle("   ");
        task.setStatus(TaskStatus.TODO);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsNull() {
        Task task = new Task();
        task.setTitle(null);
        task.setStatus(TaskStatus.TODO);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertFalse(violations.isEmpty());
    }
    
    @Test
    void shouldPassWhenTimeSpentIsNull() {
        Task task = new Task();
        task.setTitle("Valid title");
        task.setStatus(TaskStatus.TODO);
        task.setTimeSpentMinutes(null);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.isEmpty());
    }       
}