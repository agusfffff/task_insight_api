package com.example.taskanalytics.service;

import com.example.taskanalytics.dto.TaskCreateRequest;
import com.example.taskanalytics.dto.TaskResponse;
import com.example.taskanalytics.dto.TaskUpdateRequest;
import com.example.taskanalytics.dto.StatusCount;
import com.example.taskanalytics.exception.ResourceNotFoundException;
import com.example.taskanalytics.model.Task;
import com.example.taskanalytics.model.TaskStatus;
import com.example.taskanalytics.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        return convertToDTO(task);
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.TODO);

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));

        task.setTitle(request.title());
        task.setDescription(request.description());

        TaskStatus oldStatus = task.getStatus();
        TaskStatus newStatus = request.status();

        task.setStatus(newStatus);

        if (TaskStatus.DONE.equals(newStatus) && !TaskStatus.DONE.equals(oldStatus)) {
            task.setCompletedAt(LocalDateTime.now());

            if (task.getCreatedAt() != null) {
                long minutes = Duration
                    .between(task.getCreatedAt(), task.getCompletedAt())
                    .toMinutes();
                task.setTimeSpentMinutes((int) minutes);
            }
        }

        if (request.timeSpentMinutes() != null) {
            task.setTimeSpentMinutes(request.timeSpentMinutes());
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        taskRepository.delete(task);
    }

    public long getCompletedTasksCount() {
        return taskRepository.countCompletedTasks();
    }

    public Map<String, Long> getTasksCountByStatus() {
        List<StatusCount> results = taskRepository.countTasksByStatus();

        return results.stream()
            .collect(Collectors.toMap(
                r -> r.status().name(),
                StatusCount::count
            ));
    }

    public Double getAverageTimeSpent() {
        return taskRepository.averageTimeSpent();
    }

    private TaskResponse convertToDTO(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt(),
            task.getCompletedAt(),
            task.getTimeSpentMinutes()
        );
    }
}