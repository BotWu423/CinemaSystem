package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieIdAndStartTimeAfter(Long movieId, LocalDateTime now);
    List<Screening> findByScreeningRoom_Cinema_IdAndMovie_Id(Long cinemaId, Long movieId);
}