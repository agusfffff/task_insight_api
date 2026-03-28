package com.example.taskanalytics.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassWhenValidData() {
        TaskCreateRequest request = new TaskCreateRequest(
                "Valid title",
                "Valid description"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsBlank() {
        TaskCreateRequest request = new TaskCreateRequest(
                "",
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleTooShort() {
        TaskCreateRequest request = new TaskCreateRequest(
                "ab",
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("entre 3 y 255")));
    }

    @Test
    void shouldFailWhenDescriptionTooLong() {
        String longDescription = "a".repeat(1001);

        TaskCreateRequest request = new TaskCreateRequest(
                "Valid title",
                longDescription
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenTitleHasMinLength() {
        TaskCreateRequest request = new TaskCreateRequest(
                "abc",
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenTitleHasMaxLength() {
        String title = "a".repeat(255);

        TaskCreateRequest request = new TaskCreateRequest(
                title,
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleExceedsMaxLength() {
        String title = "a".repeat(256);

        TaskCreateRequest request = new TaskCreateRequest(
                title,
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenTitleIsOnlySpaces() {
        TaskCreateRequest request = new TaskCreateRequest(
                "   ",
                "Desc"
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDescriptionIsNull() {
        TaskCreateRequest request = new TaskCreateRequest(
                "Valid title",
                null
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassWhenDescriptionIsMaxLength() {
        String desc = "a".repeat(1000);

        TaskCreateRequest request = new TaskCreateRequest(
                "Valid title",
                desc
        );

        Set<ConstraintViolation<TaskCreateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}