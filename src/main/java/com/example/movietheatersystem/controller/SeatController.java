package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.SeatDTO;
import com.example.movietheatersystem.entity.Seat;
import com.example.movietheatersystem.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateSeats() {
        seatService.generateSeatsForAllRooms();
        return ResponseEntity.ok("座位已成功生成！");
    }
    @GetMapping
    public ResponseEntity<List<SeatDTO>> getSeatsByScreeningId(@RequestParam Long screeningId, Long screeningRoomId) {
        List<Seat> seats = seatService.getSeatsByScreening(screeningId);
        List<SeatDTO> dtoList = seats.stream()
                .map(seat -> new SeatDTO(
                        seat.getId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        seat.getStatus().name()))
                .toList();
        return ResponseEntity.ok(dtoList);
    }
}