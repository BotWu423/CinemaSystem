package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.ScreeningRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, Long> {
    List<ScreeningRoom> findByCinemaId(Long cinemaId);
}