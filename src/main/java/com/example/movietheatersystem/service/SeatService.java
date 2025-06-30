package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.entity.Seat;
import com.example.movietheatersystem.repository.ScreeningRepository;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import com.example.movietheatersystem.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreeningRoomRepository screeningRoomRepository;

    public SeatService(SeatRepository seatRepository,
                       ScreeningRoomRepository screeningRoomRepository) {
        this.seatRepository = seatRepository;
        this.screeningRoomRepository = screeningRoomRepository;
    }
    @Autowired
    private ScreeningRepository screeningRepository;

    @Transactional
    public void generateSeatsForAllRooms() {
        List<ScreeningRoom> rooms = screeningRoomRepository.findAll();

        for (ScreeningRoom room : rooms) {
            String[] sizeParts = room.getLayout().split("x");
            int rows = Integer.parseInt(sizeParts[0]);
            int seatsPerRow = Integer.parseInt(sizeParts[1]);

            List<Seat> seats = new ArrayList<>();

            for (int row = 1; row <= rows; row++) {
                for (int seat = 1; seat <= seatsPerRow; seat++) {
                    Seat s = new Seat();
                    s.setRowNumber(row);
                    s.setSeatNumber(seat);
                    s.setSeatType(Seat.SeatType.NORMAL); // 默认类型
                    s.setStatus(Seat.SeatStatus.AVAILABLE); // 可选状态
                    s.setScreeningRoom(room);
                    seats.add(s);
                }
            }

            seatRepository.saveAll(seats);
        }
    }
    public List<Seat> getSeatsByScreening(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("场次不存在"));

        ScreeningRoom room = screening.getScreeningRoom();

        return seatRepository.findByScreeningRoom(room);
    }
}