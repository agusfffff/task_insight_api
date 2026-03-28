package com.example.taskanalytics.service;

import com.example.taskanalytics.dto.TaskCreateRequest;
import com.example.taskanalytics.dto.TaskResponse;
import com.example.taskanalytics.dto.TaskUpdateRequest;
import com.example.taskanalytics.exception.ResourceNotFoundException;
import com.example.taskanalytics.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Test
    void shouldCreateAndRetrieveTask() {
        TaskCreateRequest request = new TaskCreateRequest("Integration Test", "Testing service layer");
        TaskResponse created = taskService.createTask(request);

        assertThat(created).isNotNull();
        assertThat(created.id()).isNotNull();
        assertThat(created.title()).isEqualTo("Integration Test");
        assertThat(created.description()).isEqualTo("Testing service layer");
        assertThat(created.status()).isEqualTo(TaskStatus.TODO);
        assertThat(created.createdAt()).isNotNull();
        assertThat(created.completedAt()).isNull();
        assertThat(created.timeSpentMinutes()).isNull();

        TaskResponse retrieved = taskService.getTaskById(created.id());
        assertThat(retrieved.id()).isEqualTo(created.id());
        assertThat(retrieved.title()).isEqualTo(created.title());
    }

    @Test
    void shouldUpdateTaskStatusToDoneAndCalculateTime() throws InterruptedException {
        TaskCreateRequest createRequest = new TaskCreateRequest("Time Test", "Testing time calculation");
        TaskResponse created = taskService.createTask(createRequest);

        Thread.sleep(100);

        TaskUpdateRequest updateRequest = new TaskUpdateRequest(
            "Time Test",
            "Testing time calculation",
            TaskStatus.DONE,
            30 
        );
        TaskResponse updated = taskService.updateTask(created.id(), updateRequest);

        assertThat(updated.status()).isEqualTo(TaskStatus.DONE);
        assertThat(updated.completedAt()).isNotNull();
        assertThat(updated.timeSpentMinutes()).isEqualTo(30);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        assertThatThrownBy(() -> taskService.getTaskById(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Tarea no encontrada con id: 999");
    }

    @Test
    void shouldDeleteTask() {
        TaskCreateRequest request = new TaskCreateRequest("Delete Test", "To be deleted");
        TaskResponse created = taskService.createTask(request);

        taskService.deleteTask(created.id());

        assertThatThrownBy(() -> taskService.getTaskById(created.id()))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldGetStatistics() {
        taskService.createTask(new TaskCreateRequest("Task 1", "Desc 1"));
        taskService.createTask(new TaskCreateRequest("Task 2", "Desc 2"));

        TaskResponse task3 = taskService.createTask(new TaskCreateRequest("Task 3", "Desc 3"));
        taskService.updateTask(task3.id(), new TaskUpdateRequest("Task 3", "Desc 3", TaskStatus.DONE, null));

        long completedCount = taskService.getCompletedTasksCount();
        Map<String, Long> statusStats = taskService.getTasksCountByStatus();
        Double avgTime = taskService.getAverageTimeSpent();

        assertThat(completedCount).isEqualTo(1);
        assertThat(statusStats.get("TODO")).isEqualTo(2L);
        assertThat(statusStats.get("DONE")).isEqualTo(1L);
        assertThat(avgTime).isNotNull();
    }

    @Test
    void shouldNotSetCompletedAtWhenStatusIsNotDone() {
        TaskResponse created = taskService.createTask(
            new TaskCreateRequest("Task", "Desc")
        );

        TaskResponse updated = taskService.updateTask(
            created.id(),
            new TaskUpdateRequest("Task", "Desc", TaskStatus.IN_PROGRESS, null)
        );

        assertThat(updated.completedAt()).isNull();
        assertThat(updated.timeSpentMinutes()).isNull();
    }    

    @Test
    void shouldUpdateFromDoneToAnotherStatusWithoutOverwritingCompletedAt() {
        TaskResponse created = taskService.createTask(
            new TaskCreateRequest("Task", "Desc")
        );

        taskService.updateTask(
            created.id(),
            new TaskUpdateRequest("Task", "Desc", TaskStatus.DONE, 50)
        );

        TaskResponse reverted = taskService.updateTask(
            created.id(),
            new TaskUpdateRequest("Task", "Desc", TaskStatus.TODO, 20)
        );

        assertThat(reverted.status()).isEqualTo(TaskStatus.TODO);
        assertThat(reverted.completedAt()).isNotNull(); 
    }    

    @Test
    void shouldOverrideTimeSpentMinutes() {
        TaskResponse created = taskService.createTask(
            new TaskCreateRequest("Task", "Desc")
        );

        TaskResponse updated = taskService.updateTask(
            created.id(),
            new TaskUpdateRequest("Task", "Desc", TaskStatus.TODO, 100)
        );

        assertThat(updated.timeSpentMinutes()).isEqualTo(100);
    }    


    @Test
    void shouldIgnoreNullTimeSpentInAverage() {
        TaskResponse t1 = taskService.createTask(
            new TaskCreateRequest("T1", "Desc")
        );

        taskService.updateTask(
            t1.id(),
            new TaskUpdateRequest("T1", "Desc", TaskStatus.DONE, 10)
        );

        taskService.createTask(
            new TaskCreateRequest("T2", "Desc")
        );

        Double avg = taskService.getAverageTimeSpent();

        assertThat(avg).isEqualTo(10.0);
    }    

    @Test
    void shouldThrowWhenDeletingNonExistentTask() {
        assertThatThrownBy(() -> taskService.deleteTask(999L))
            .isInstanceOf(ResourceNotFoundException.class);
    }   
}