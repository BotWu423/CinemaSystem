package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.CreateOrderDTO;
import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderDTO dto) {
        try {
            Order order = orderService.createOrder(
                    dto.getUserId(),
                    dto.getScreeningId(),
                    dto.getSeatIds(),
                    dto.getTotalPrice()
            );
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            // 记录详细的错误信息
            log.error("创建订单失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
    @GetMapping("/booked-seats")
    public ResponseEntity<List<Long>> getBookedSeatIdsByScreeningId(@RequestParam Long screeningId) {
        List<Long> bookedSeatIds = orderService.getBookedSeatIdsByScreeningId(screeningId);
        return ResponseEntity.ok(bookedSeatIds);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}