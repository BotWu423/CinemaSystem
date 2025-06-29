package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.service.CinemaService;
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

    /**
     * 根据电影ID获取可用影院（有未开始场次的影院）
     */
    @GetMapping
    public ResponseEntity<List<Cinema>> getCinemasByMovieId(@RequestParam("movieId") Long movieId) {
        List<Cinema> cinemas = cinemaService.getCinemasByMovieId(movieId);
        return ResponseEntity.ok(cinemas);
    }
}