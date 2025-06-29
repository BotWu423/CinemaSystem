package com.example.movietheatersystem.dto;

public class SeatDTO {
    private Long id;
    private int rowNumber;
    private int seatNumber;
    private String status;

    public SeatDTO(Long id, int rowNumber, int seatNumber, String status) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getter and Setter
}