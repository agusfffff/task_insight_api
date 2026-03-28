package com.example.taskanalytics.service;

import com.example.taskanalytics.dto.TaskCreateRequest;
import com.example.taskanalytics.dto.TaskResponse;
import com.example.taskanalytics.dto.TaskUpdateRequest;
import com.example.taskanalytics.exception.ResourceNotFoundException;
import com.example.taskanalytics.model.Task;
import com.example.taskanalytics.model.TaskStatus;
import com.example.taskanalytics.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

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

    public TaskResponse createTask(TaskCreateRequest createTaskDTO) {
        Task task = new Task();
        task.setTitle(createTaskDTO.title());
        task.setDescription(createTaskDTO.description());
        task.setStatus(TaskStatus.TODO);
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest updateTaskDTO) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        
        task.setTitle(updateTaskDTO.title());
        task.setDescription(updateTaskDTO.description());
        
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(updateTaskDTO.status());
        
        if (TaskStatus.DONE.equals(updateTaskDTO.status()) && !TaskStatus.DONE.equals(oldStatus)) {
            task.setCompletedAt(LocalDateTime.now());
            if (task.getCreatedAt() != null) {
                long minutes = Duration.between(task.getCreatedAt(), task.getCompletedAt()).toMinutes();
                task.setTimeSpentMinutes((int) minutes);
            }
        }
        
        if (updateTaskDTO.timeSpentMinutes() != null) {
            task.setTimeSpentMinutes(updateTaskDTO.timeSpentMinutes());
        }
        
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con id: " + id));
        taskRepository.deleteById(id);
    }

    public long getCompletedTasksCount() {
        return taskRepository.countCompletedTasks();
    }

    public Map<String, Long> getTasksCountByStatus() {
        return taskRepository.countTasksByStatus();
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