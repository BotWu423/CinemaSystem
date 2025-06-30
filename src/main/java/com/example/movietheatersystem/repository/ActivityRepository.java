package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}