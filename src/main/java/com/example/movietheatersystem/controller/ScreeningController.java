package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.entity.Seat;
import com.example.movietheatersystem.service.AdminService;
import com.example.movietheatersystem.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screenings")
@CrossOrigin(origins = "*")
public class ScreeningController {
    @Autowired
    private ScreeningService screeningService;
    @GetMapping
    public ResponseEntity<List<Screening>> getScreeningsByCinemaAndMovie(
            @RequestParam Long cinemaId,
            @RequestParam Long movieId) {
        List<Screening> screenings = screeningService.getScreeningsByCinemaAndMovie(cinemaId, movieId);
        return ResponseEntity.ok(screenings);
    }

}
