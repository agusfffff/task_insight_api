package com.example.taskanalytics.dto;

import com.example.taskanalytics.model.TaskStatus;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskUpdateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassWhenValidData() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                "Valid description",
                TaskStatus.TODO,
                30
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsNull() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                null,
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsBlank() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "   ",
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleTooShort() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "ab",
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("entre 3 y 255")));
    }

    @Test
    void shouldPassWhenTitleAtMinLength() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "abc",
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenTitleAtMaxLength() {
        String title = "a".repeat(255);

        TaskUpdateRequest request = new TaskUpdateRequest(
                title,
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleExceedsMaxLength() {
        String title = "a".repeat(256);

        TaskUpdateRequest request = new TaskUpdateRequest(
                title,
                "Desc",
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDescriptionIsNull() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                null,
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDescriptionIsMaxLength() {
        String desc = "a".repeat(1000);

        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                desc,
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        String desc = "a".repeat(1001);

        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                desc,
                TaskStatus.TODO,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenStatusIsNull() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                "Desc",
                null,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenStatusIsValid() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                "Desc",
                TaskStatus.IN_PROGRESS,
                10
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenTimeSpentIsNull() {
        TaskUpdateRequest request = new TaskUpdateRequest(
                "Valid title",
                "Desc",
                TaskStatus.TODO,
                null
        );

        Set<ConstraintViolation<TaskUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}