package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    public List<Activity> findByParticipantsId(Long userId);
    @Modifying
    @Query("DELETE FROM Activity a WHERE a.cinemaId = :cinemaId")
    void deleteByCinemaId(@Param("cinemaId") Long cinemaId);

}