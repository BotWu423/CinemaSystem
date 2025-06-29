package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    @Query(value = """
        SELECT DISTINCT c.* FROM cinemas c
        JOIN screening_rooms sr ON c.id = sr.cinema_id
        JOIN screenings s ON sr.id = s.screening_room_id
        WHERE s.movie_id = ?1
          AND s.start_time > NOW()
          AND s.status != 'FINISHED'
        """, nativeQuery = true)
    List<Cinema> findAllByIdInAndHasFutureScreenings(Long movieId);
}