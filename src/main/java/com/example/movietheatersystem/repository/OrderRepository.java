package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByScreeningId(Long screeningId);

}