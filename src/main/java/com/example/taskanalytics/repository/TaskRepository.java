package com.example.taskanalytics.repository;

import com.example.taskanalytics.dto.StatusCount;
import com.example.taskanalytics.model.Task;
import com.example.taskanalytics.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByStatus(TaskStatus status);

    default long countCompletedTasks() {
        return countByStatus(TaskStatus.DONE);
    }

    @Query("""
        SELECT new com.example.taskanalytics.dto.StatusCount(t.status, COUNT(t))
        FROM Task t
        GROUP BY t.status
    """)
    List<StatusCount> countTasksByStatus();

    @Query("""
        SELECT AVG(t.timeSpentMinutes)
        FROM Task t
        WHERE t.status = :status
          AND t.timeSpentMinutes IS NOT NULL
    """)
    Double averageTimeSpentByStatus(TaskStatus status);

    default Double averageTimeSpent() {
        return averageTimeSpentByStatus(TaskStatus.DONE);
    }
}