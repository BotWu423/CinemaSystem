package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAll();
    @Query(value = """
    SELECT DISTINCT m.* FROM movies m
    JOIN screenings s ON m.id = s.movie_id
    WHERE s.screening_room_id IN (
        SELECT id FROM screening_rooms WHERE cinema_id = ?1
    )
    AND s.start_time > NOW()
    AND s.status != 'FINISHED'
    """, nativeQuery = true)
    List<Movie> findCurrentMoviesByCinemaId(Long cinemaId);

}