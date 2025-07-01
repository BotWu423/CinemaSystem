package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
} 