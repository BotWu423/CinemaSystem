package com.example.movietheatersystem.service;

import com.example.movietheatersystem.repository.CinemaCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaCommentService {

    @Autowired
    CinemaCommentRepository cinemaCommentRepository;

    @Transactional
    public void deleteByCinemaId(Long cinemaId) {
        cinemaCommentRepository.deleteByCinemaId(cinemaId);
    }
}
