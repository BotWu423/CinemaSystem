package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAll();
}