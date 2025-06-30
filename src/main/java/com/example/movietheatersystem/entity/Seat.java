package com.example.movietheatersystem.entity;
import com.example.movietheatersystem.tool.SeatStatusConverter;
import com.example.movietheatersystem.tool.SeatTypeConverter;
import jakarta.persistence.*;
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ScreeningRoom screeningRoom;
    @Column(name = "`rowNumber`")
    private int rowNumber;

    @Column(name = "`seatNumber`")
    private int seatNumber;

    @Column(columnDefinition = "VARCHAR(20)")
    @Convert(converter = SeatTypeConverter.class)
    private SeatType seatType;
    public enum SeatType {
        NORMAL, COUPLE, DISABLED
    }
//废弃属性
    @Column(columnDefinition = "VARCHAR(20)")
    @Convert(converter = SeatStatusConverter.class)
    private SeatStatus status;
    public enum SeatStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScreeningRoom getScreeningRoom() {
        return screeningRoom;
    }

    public void setScreeningRoom(ScreeningRoom screeningRoom) {
        this.screeningRoom = screeningRoom;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
// Getters and Setters
}