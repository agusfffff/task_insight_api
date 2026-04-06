package com.example.taskanalytics.controller;

import com.example.taskanalytics.dto.TaskCreateRequest;
import com.example.taskanalytics.dto.TaskResponse;
import com.example.taskanalytics.dto.TaskUpdateRequest;
import com.example.taskanalytics.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) { 
        this.taskService = taskService; 
    }
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest createTaskDTO) {
        TaskResponse createdTask = taskService.createTask(createTaskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest updateTaskDTO) {
        TaskResponse updatedTask = taskService.updateTask(id, updateTaskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/completed")
    public ResponseEntity<Long> getCompletedTasksCount() {
        long count = taskService.getCompletedTasksCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<Map<String, Long>> getTasksCountByStatus() {
        Map<String, Long> stats = taskService.getTasksCountByStatus();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/avg-time")
    public ResponseEntity<Double> getAverageTimeSpent() {
        Double avg = taskService.getAverageTimeSpent();
        return ResponseEntity.ok(avg);
    }
}