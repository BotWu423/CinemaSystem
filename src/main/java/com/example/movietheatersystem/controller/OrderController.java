package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Order;
import com.example.movietheatersystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> payload) {
        // 从 Map 中取出参数
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long screeningId = Long.valueOf(payload.get("screeningId").toString());
        List<Long> seatIds = (List<Long>) payload.get("seatIds");
        String totalPriceStr = payload.get("totalPrice").toString();
        BigDecimal totalPrice = new BigDecimal(totalPriceStr);

        Order order = orderService.createOrder(userId, screeningId, seatIds, totalPrice);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}