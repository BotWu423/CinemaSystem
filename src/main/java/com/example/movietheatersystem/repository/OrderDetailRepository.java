package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od.seat.id FROM OrderDetail od WHERE od.order.screening.id = :screeningId")
    List<Long> findBookedSeatIdsByScreeningId(@Param("screeningId") Long screeningId);
}