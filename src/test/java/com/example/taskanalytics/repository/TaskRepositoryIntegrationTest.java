package com.example.taskanalytics.repository;

import com.example.taskanalytics.dto.StatusCount;
import com.example.taskanalytics.model.Task;
import com.example.taskanalytics.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;


    @Test
    void shouldSaveAndFindTask() {
        Task task = new Task();
        task.setTitle("Repository Test");
        task.setDescription("Testing repository layer");
        task.setStatus(TaskStatus.TODO);

        Task saved = taskRepository.save(task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Repository Test");
        assertThat(saved.getStatus()).isEqualTo(TaskStatus.TODO);

        Task found = taskRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Repository Test");
    }

    @Test
    void shouldCountCompletedTasks() {
        Task todoTask = new Task();
        todoTask.setTitle("Todo");
        todoTask.setStatus(TaskStatus.TODO);
        taskRepository.save(todoTask);

        Task doneTask = new Task();
        doneTask.setTitle("Done");
        doneTask.setStatus(TaskStatus.DONE);
        taskRepository.save(doneTask);

        long completedCount = taskRepository.countCompletedTasks();

        assertThat(completedCount).isEqualTo(1);
    }

    @Test
    void shouldCountTasksByStatus() {
        Task todoTask = new Task();
        todoTask.setTitle("Todo");
        todoTask.setStatus(TaskStatus.TODO);
        taskRepository.save(todoTask);

        Task inProgressTask = new Task();
        inProgressTask.setTitle("In Progress");
        inProgressTask.setStatus(TaskStatus.IN_PROGRESS);
        taskRepository.save(inProgressTask);

        Task doneTask = new Task();
        doneTask.setTitle("Done");
        doneTask.setStatus(TaskStatus.DONE);
        taskRepository.save(doneTask);

        List<StatusCount> results = taskRepository.countTasksByStatus();

        Map<String, Long> stats = results.stream()
            .collect(Collectors.toMap(
                r -> r.status().name(),
                StatusCount::count
            ));

        assertThat(stats.get("TODO")).isEqualTo(1L);
        assertThat(stats.get("IN_PROGRESS")).isEqualTo(1L);
        assertThat(stats.get("DONE")).isEqualTo(1L);
    }

    @Test
    void shouldCalculateAverageTimeSpent() {
        Task doneTask1 = new Task();
        doneTask1.setTitle("Done 1");
        doneTask1.setStatus(TaskStatus.DONE);
        doneTask1.setTimeSpentMinutes(30);
        taskRepository.save(doneTask1);

        Task doneTask2 = new Task();
        doneTask2.setTitle("Done 2");
        doneTask2.setStatus(TaskStatus.DONE);
        doneTask2.setTimeSpentMinutes(60);
        taskRepository.save(doneTask2);

        Task todoTask = new Task();
        todoTask.setTitle("Todo");
        todoTask.setStatus(TaskStatus.TODO);
        taskRepository.save(todoTask);

        Double average = taskRepository.averageTimeSpent();

        assertThat(average).isEqualTo(45.0);
    }

    @Test
    void shouldHandleEmptyAverageTimeSpent() {
        Double average = taskRepository.averageTimeSpent();

        assertThat(average).isNull();
    }
}