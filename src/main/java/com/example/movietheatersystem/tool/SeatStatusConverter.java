package com.example.movietheatersystem.tool;

import com.example.movietheatersystem.entity.Seat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SeatStatusConverter implements AttributeConverter<Seat.SeatStatus, String> {

    @Override
    public String convertToDatabaseColumn(Seat.SeatStatus attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public Seat.SeatStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Seat.SeatStatus.valueOf(dbData);
    }
}