package com.example.movietheatersystem.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CreateOrderDTO {
    @NotNull(message = "userId 不能为空")
    private Long userId;

    @NotNull(message = "screeningId 不能为空")
    private Long screeningId;

    @NotNull(message = "seatIds 不能为空")
    private List<Long> seatIds;

    @NotNull(message = "totalPrice 不能为空")
    private BigDecimal totalPrice;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getScreeningId() { return screeningId; }
    public void setScreeningId(Long screeningId) { this.screeningId = screeningId; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}