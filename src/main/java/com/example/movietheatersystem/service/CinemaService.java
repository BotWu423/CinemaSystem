package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.repository.CinemaRepository;
import com.example.movietheatersystem.repository.ScreeningRepository;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    public List<Cinema> getCinemasByMovieId(Long movieId) {
        return cinemaRepository.findAllByIdInAndHasFutureScreenings(movieId);
    }
}