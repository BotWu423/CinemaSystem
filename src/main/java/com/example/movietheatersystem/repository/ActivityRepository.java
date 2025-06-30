package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    public List<Activity> findByParticipantsId(Long userId);
}