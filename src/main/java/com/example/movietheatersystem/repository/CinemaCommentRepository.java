package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.CinemaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaCommentRepository extends JpaRepository<CinemaComment, Long> {
    List<CinemaComment> findByCinemaId(Long cinemaId);
    List<CinemaComment> findByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM CinemaComment cc WHERE cc.cinema.id = :cinemaId")
    void deleteByCinemaId(@Param("cinemaId") Long cinemaId);

}
