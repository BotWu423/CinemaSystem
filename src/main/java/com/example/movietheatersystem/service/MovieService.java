package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}