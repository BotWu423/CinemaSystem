package com.example.movietheatersystem.dto;

public class AddScreeningRoomRequest {
    private String name;
    private Long cinemaId; // 用于关联影院
    private String layout; // 座位布局，如 "5x8"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
