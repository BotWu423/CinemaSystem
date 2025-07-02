package com.example.movietheatersystem.service;

import com.example.movietheatersystem.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Transactional
    public void deleteCommentsByMovieId(Long MovieId)
    {
        commentRepository.deleteCommentsByMovieId(MovieId);
    }
}
