package com.example.movietheatersystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderAdminDTO {
    private Long id;
    private String username;
    private String movieTitle;
    private String cinemaName;
    private String screeningRoomName;
    private LocalDateTime screeningTime;
    private String seatInfo;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
    private String status;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public void setCinemaName(String cinemaName) { this.cinemaName = cinemaName; }
    public String getScreeningRoomName() { return screeningRoomName; }
    public void setScreeningRoomName(String screeningRoomName) { this.screeningRoomName = screeningRoomName; }
    public LocalDateTime getScreeningTime() { return screeningTime; }
    public void setScreeningTime(LocalDateTime screeningTime) { this.screeningTime = screeningTime; }
    public String getSeatInfo() { return seatInfo; }
    public void setSeatInfo(String seatInfo) { this.seatInfo = seatInfo; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 