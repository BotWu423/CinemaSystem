package com.example.movietheatersystem.service;

import com.example.movietheatersystem.dto.ScreeningRoomDTO;
import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.repository.OrderRepository;
import com.example.movietheatersystem.repository.ScreeningRepository;
import com.example.movietheatersystem.repository.ScreeningRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningRoomService {

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private SeatService seatService;

    public List<ScreeningRoomDTO> getAllScreeningRooms() {
        return screeningRoomRepository.findAllWithCinemaName();
    }
    public Optional<ScreeningRoom> getScreeningRoomById(Long id) {
        return screeningRoomRepository.findById(id);
    }
    @Transactional
    public void deleteScreeningRoomWithDependencies(Long roomId) {
        List<Screening> screenings = screeningRepository.findByScreeningRoomId(roomId);
        for(Screening screening :screenings) {
            List<Order> orders = orderRepository.findByScreeningId(screening.getId());
            for (Order order : orders) {
                orderService.cancelOrder(order.getId());
            }
        }
        for (Screening screening : screenings) {
            screeningService.deleteScreening(screening.getId());
        }
        seatService.deleteSeatsByScreeningRoomId(roomId);
        screeningRoomRepository.deleteById(roomId);
    }
    @Transactional
    public void deleteBycinemaId(Long Id)
    {
        List<ScreeningRoom> screeningRooms = screeningRoomRepository.findByCinemaId(Id);
        for (ScreeningRoom screeningRoom : screeningRooms) {
            deleteScreeningRoomWithDependencies(screeningRoom.getId());
        }
    }
}
