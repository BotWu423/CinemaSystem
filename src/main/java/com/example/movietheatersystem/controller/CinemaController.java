package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.service.ActivityService;
import com.example.movietheatersystem.service.CinemaCommentService;
import com.example.movietheatersystem.service.CinemaService;
import com.example.movietheatersystem.service.ScreeningRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@CrossOrigin(origins = "*")
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private ScreeningRoomService screeningRoomService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CinemaCommentService cinemaCommentService;
    @GetMapping("/all")
    public ResponseEntity<List<Cinema>> getAllCinemas() {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        return ResponseEntity.ok(cinemas);
    }
    @GetMapping
    public ResponseEntity<List<Cinema>> getCinemasByMovieId(@RequestParam("movieId") Long movieId) {
        List<Cinema> cinemas = cinemaService.getCinemasByMovieId(movieId);
        return ResponseEntity.ok(cinemas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cinema> getCinemaById(@PathVariable Long id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return ResponseEntity.ok(cinema);
    }

    @GetMapping("/cinema-detail")
    public ResponseEntity<Cinema> getCinemaDetail(@RequestParam Long cinemaId) {
        Cinema cinema = cinemaService.getCinemaById(cinemaId);
        return ResponseEntity.ok(cinema);
    }

    @DeleteMapping("/{cinemaId}")
    public ResponseEntity<String> deleteCinema(@PathVariable Long cinemaId) {
        cinemaCommentService.deleteByCinemaId(cinemaId);
        activityService.deleteByCinemaId(cinemaId);
        screeningRoomService.deleteBycinemaId(cinemaId);
        cinemaService.deleteCinema(cinemaId);
        return ResponseEntity.ok("影院及其数据已成功删除");
    }
    @PutMapping
    public ResponseEntity<Cinema> updateCinema(@RequestBody Cinema cinema) {
        return ResponseEntity.ok(cinemaService.updateCinema(cinema));
    }

}