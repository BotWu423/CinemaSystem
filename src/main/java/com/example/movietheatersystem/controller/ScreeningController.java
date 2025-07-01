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
    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(@PathVariable Long id) {
        Screening screening = screeningService.getScreeningById(id);
        if (screening == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(screening);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Screening>> getAllScreenings() {
        List<Screening> screenings = screeningService.getAllScreenings();
        return ResponseEntity.ok(screenings);
    }
    // 添加新场次（管理员权限）
}