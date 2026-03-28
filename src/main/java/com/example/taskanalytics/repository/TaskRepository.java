package com.example.taskanalytics.repository;

import com.example.taskanalytics.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = com.example.taskanalytics.model.TaskStatus.DONE")
    long countCompletedTasks();

    @Query("SELECT t.status as status, COUNT(t) as count FROM Task t GROUP BY t.status")
    Map<String, Long> countTasksByStatus();

    @Query("SELECT AVG(t.timeSpentMinutes) FROM Task t WHERE t.status = com.example.taskanalytics.model.TaskStatus.DONE AND t.timeSpentMinutes IS NOT NULL")
    Double averageTimeSpent();
}