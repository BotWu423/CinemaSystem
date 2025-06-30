package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.service.AdminService;
import com.example.movietheatersystem.service.CinemaService;
import com.example.movietheatersystem.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CinemaService cinemaService;

    // 添加放映厅
    @PostMapping("/rooms")
    public ResponseEntity<ScreeningRoom> addScreeningRoom(@RequestBody ScreeningRoom screeningRoom) {
        return ResponseEntity.ok(adminService.addScreeningRoom(screeningRoom));
    }

    // 更新座位布局
    @PutMapping("/rooms/{roomId}/layout")
    public ResponseEntity<?> updateSeatLayout(@PathVariable Long roomId, @RequestBody Map<String, Object> payload) {
        String layoutJson = payload.get("layoutJson").toString();
        adminService.updateSeatLayout(roomId, layoutJson);
        return ResponseEntity.ok().build();
    }
    @PostMapping("movie-management/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }
    @PostMapping("/cinemas-management/add")
    public ResponseEntity<Cinema> addCinema(@RequestBody Cinema cinema) {
        return ResponseEntity.ok(cinemaService.addCinema(cinema));
    }
}