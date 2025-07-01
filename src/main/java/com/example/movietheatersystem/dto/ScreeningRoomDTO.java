package com.example.movietheatersystem.dto;

public class ScreeningRoomDTO {
    private Long id;
    private String name;
    private String cinemaName;

    public ScreeningRoomDTO(Long id, String name, String cinemaName) {
        this.id = id;
        this.name = name;
        this.cinemaName = cinemaName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
// Getters and Setters
}
