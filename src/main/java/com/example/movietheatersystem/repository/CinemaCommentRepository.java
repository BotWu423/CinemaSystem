package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.CinemaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CinemaCommentRepository extends JpaRepository<CinemaComment, Long> {
    List<CinemaComment> findByCinemaId(Long cinemaId);
    List<CinemaComment> findByUserId(Long userId);
}
