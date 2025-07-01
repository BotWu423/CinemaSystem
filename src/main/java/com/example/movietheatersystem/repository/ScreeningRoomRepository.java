package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.dto.ScreeningRoomDTO;
import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.ScreeningRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, Long> {
    @Query("SELECT new com.example.movietheatersystem.dto.ScreeningRoomDTO(r.id, r.name, r.cinema.name) FROM ScreeningRoom r JOIN r.cinema c")
    List<ScreeningRoomDTO> findAllWithCinemaName();
}