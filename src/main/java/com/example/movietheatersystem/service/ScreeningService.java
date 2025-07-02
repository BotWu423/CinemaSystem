package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.*;
import com.example.movietheatersystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.movietheatersystem.dto.UpdateScreeningDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScreeningService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private SeatRepository seatRepository;
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<Screening> getScreeningsByCinemaAndMovie(Long cinemaId, Long movieId) {
        return screeningRepository.findByScreeningRoom_Cinema_IdAndMovie_Id(cinemaId, movieId);
    }
    public List<Screening> getUpcomingScreeningsForMovie(Long movieId) {
        return screeningRepository.findByMovieIdAndStartTimeAfter(movieId, LocalDateTime.now());
    }

    public List<Seat> getAvailableSeatsForScreening(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("场次不存在"));

        return seatRepository.findByScreeningRoomIdAndStatus(
                screening.getScreeningRoom().getId(),
                Seat.SeatStatus.AVAILABLE
        );
    }

    public Order createOrder(Long userId, Long screeningId, List<Long> seatIds, BigDecimal totalPrice) {
        // 检查座位是否可用
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("座位不存在"));

            if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                throw new RuntimeException("座位" + seat.getRowNumber() + "-" + seat.getSeatNumber() + "已被选");
            }

            seat.setStatus(Seat.SeatStatus.OCCUPIED);
            seatRepository.save(seat);
        }

        // 创建订单
        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在")));
        order.setScreening(screeningRepository.findById(screeningId).orElseThrow(() -> new RuntimeException("场次不存在")));
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreateTime(LocalDateTime.now());

        // 创建订单详情
        List<OrderDetail> details = new ArrayList<>();
        for (Long seatId : seatIds) {
            OrderDetail detail = new OrderDetail();
            detail.setSeat(seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("座位不存在")));
            detail.setPrice(order.getScreening().getPrice());
            detail.setOrder(order);
            details.add(detail);
        }

        order.setDetails(details);
        return orderRepository.save(order);
    }
    public List<Screening> getAllMovies() {
        return screeningRepository.findAll(); // 返回所有场次信息
    }
    public Screening getScreeningById(Long id) {
        return screeningRepository.findById(id).orElse(null);
    }
    public List<Screening> getAllScreenings(){
        return screeningRepository.findAll();
    }
    public List<ScreeningRoom> getAllScreeningRooms()
    {
        return screeningRoomRepository.findAll();
    }
    // 根据放映室 ID 删除所有场次
    @Transactional
    public void deleteScreening(Long screeningId) {
        screeningRepository.deleteById(screeningId);
    }

    public List<Screening> findByScreeningRoomId(Long roomId) {
        return screeningRepository.findByScreeningRoomId(roomId);
    }
    public List<Screening> findByMovieId(Long MovieId) {
        return screeningRepository.findAllByMovieId(MovieId);
    }
    @Transactional
    public Screening updateScreening(UpdateScreeningDTO dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("场次ID不能为空");
        }

        Screening screening = screeningRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("场次不存在"));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("电影不存在"));

        ScreeningRoom room = screeningRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("放映厅不存在"));

        // 更新字段
        screening.setMovie(movie);
        screening.setScreeningRoom(room);
        screening.setStartTime(dto.getStartTime());
        screening.setPrice(dto.getPrice());

        return screeningRepository.save(screening);
    }
}