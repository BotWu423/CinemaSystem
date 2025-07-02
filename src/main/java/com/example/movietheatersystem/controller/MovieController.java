package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.FeaturedMovieDTO;
import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.service.CommentService;
import com.example.movietheatersystem.service.MovieService;
import com.example.movietheatersystem.service.OrderService;
import com.example.movietheatersystem.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrderService orderService;
    // 获取所有电影
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // 根据 ID 获取单部电影
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    // 新增接口：获取轮播电影列表
    @GetMapping("/featured")
    public ResponseEntity<List<FeaturedMovieDTO>> getFeaturedMovies() {
        return ResponseEntity.ok(movieService.getFeaturedMovies());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        commentService.deleteCommentsByMovieId(id);
        List<Screening> screenings= screeningService.findByMovieId(id);
        for(Screening screening : screenings)
        {
            List<Order> orders = orderService.findByScreeningId(screening.getId());
            for (Order order : orders) {
                orderService.cancelOrder(order.getId());
            }
            screeningService.deleteScreening(screening.getId());
        }
        movieService.deleteMovieById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(movie));
    }

}