package com.example.movietheatersystem.service;

import com.example.movietheatersystem.dto.ScreeningRoomDTO;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningRoomService {

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    public List<ScreeningRoomDTO> getAllScreeningRooms() {
        return screeningRoomRepository.findAllWithCinemaName();
    }
    public Optional<ScreeningRoom> getScreeningRoomById(Long id) {
        return screeningRoomRepository.findById(id);
    }
}
