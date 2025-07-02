package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.entity.OrderDetail;
import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.Seat;
import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.repository.*;
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

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Order createOrder(Long userId, Long screeningId, List<Long> seatIds, BigDecimal totalPrice) {
        // 检查座位是否可用
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("座位不存在"));

            if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                throw new RuntimeException("座位" + seat.getRowNumber() + "-" + seat.getSeatNumber() + "已被选");
            }

            seat.setStatus(Seat.SeatStatus.AVAILABLE);
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

        // 更新电影热度和票房
        Movie movie = order.getScreening().getMovie();
        if (movie != null) {
            int addPopularity = seatIds.size() * 10;
            BigDecimal addBoxOffice = totalPrice;
            movie.setPopularity(movie.getPopularity() + addPopularity);
            movie.setBoxOffice(movie.getBoxOffice().add(addBoxOffice));
            movieRepository.save(movie);
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    public List<Long> getBookedSeatIdsByScreeningId(Long screeningId) {
        return orderDetailRepository.findBookedSeatIdsByScreeningId(screeningId);
    }
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在，ID: " + orderId));
    }
    // 取消订单的方法
    public void cancelOrder(Long orderId) {
        // 1. 先删除订单详情
        orderDetailRepository.deleteByOrderId(orderId);

        // 2. 再删除主订单
        orderRepository.deleteById(orderId);
    }
    public List<Order> findByScreeningId(Long roomId) {
        return orderRepository.findByScreeningId(roomId);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
