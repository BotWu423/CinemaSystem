package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {
    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    public ScreeningRoom addScreeningRoom(ScreeningRoom screeningRoom) {
        return screeningRoomRepository.save(screeningRoom);
    }

    public void updateSeatLayout(Long screeningRoomId, String layoutJson) {
        ScreeningRoom room = screeningRoomRepository.findById(screeningRoomId)
                .orElseThrow(() -> new RuntimeException("放映室不存在"));

        room.setLayout(layoutJson);
        screeningRoomRepository.save(room);
    }
}