package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.ScreeningRoomDTO;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.service.ScreeningRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/screening-rooms")
@CrossOrigin(origins = "*")
public class ScreeningRoomController {

    @Autowired
    private ScreeningRoomService screeningRoomService;

    @GetMapping("/all")
    public ResponseEntity<List<ScreeningRoomDTO>> getAllScreeningRooms() {
        List<ScreeningRoomDTO> rooms = screeningRoomService.getAllScreeningRooms();
        return ResponseEntity.ok(rooms);
    }

}
