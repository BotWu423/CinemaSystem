package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreeningRoomIdAndStatus(Long screeningRoomId, Seat.SeatStatus status);
    List<Seat> findByScreeningRoom(ScreeningRoom screeningRoom);
    void deleteByScreeningRoomId(Long roomId);

}