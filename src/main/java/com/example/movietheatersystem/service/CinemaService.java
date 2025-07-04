package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.repository.CinemaRepository;
import com.example.movietheatersystem.repository.ScreeningRepository;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Optional<Cinema> getCinemaById1(Long id) {
        return cinemaRepository.findById(id);
    }

    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }
    public Cinema addCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
    public Cinema getCinemaById(Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("影院不存在，ID: " + id));
    }
    public void deleteCinema(Long id) {
        cinemaRepository.deleteById(id);
    }
    @Transactional
    public Cinema updateCinema(Cinema cinema) {
        if (cinema == null || cinema.getId() == null) {
            throw new IllegalArgumentException("影院ID不能为空");
        }

        if (cinema.getName() == null || cinema.getName().isEmpty()) {
            throw new IllegalArgumentException("影院名称不能为空");
        }

        return cinemaRepository.save(cinema);
    }

}