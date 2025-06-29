package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.OrderDetail;
import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.Seat;
import com.example.movietheatersystem.repository.OrderRepository;
import com.example.movietheatersystem.repository.ScreeningRepository;
import com.example.movietheatersystem.repository.SeatRepository;
import com.example.movietheatersystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

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

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
