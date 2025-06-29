package com.example.movietheatersystem.tool;

import com.example.movietheatersystem.entity.Seat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SeatTypeConverter implements AttributeConverter<Seat.SeatType, String> {

    @Override
    public String convertToDatabaseColumn(Seat.SeatType attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public Seat.SeatType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Seat.SeatType.valueOf(dbData);
    }
}