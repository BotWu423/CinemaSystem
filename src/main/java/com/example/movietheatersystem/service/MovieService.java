package com.example.movietheatersystem.service;

import com.example.movietheatersystem.dto.FeaturedMovieDTO;
import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll(); // 获取所有电影
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null); // 根据ID获取电影
    }
    @Transactional
    public Movie addMovie(Movie movie) {
        if (movie == null || movie.getTitle() == null || movie.getTitle().isEmpty()) {
            throw new IllegalArgumentException("电影标题不能为空");
        }
        return movieRepository.save(movie);
    }
    // 新增方法：获取轮播电影列表
    public List<FeaturedMovieDTO> getFeaturedMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> new FeaturedMovieDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getPosterUrl()))
                .collect(Collectors.toList());
    }
}