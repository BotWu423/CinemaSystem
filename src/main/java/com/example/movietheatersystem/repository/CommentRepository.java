package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieId(Long movieId);
    List<Comment> findByUserId(Long userId);
} 