package com.example.taskanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskAnalyticsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskAnalyticsApiApplication.class, args);
    }

}