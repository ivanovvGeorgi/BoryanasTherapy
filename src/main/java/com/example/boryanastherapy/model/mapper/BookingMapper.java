package com.example.boryanastherapy.model.mapper;

import com.example.boryanastherapy.model.dto.BookingDTO;
import com.example.boryanastherapy.model.entity.Booking;

public class BookingMapper {

    public static Booking toEntity(BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setName(bookingDTO.getName());
        booking.setEmail(bookingDTO.getEmail());
        booking.setDate(bookingDTO.getDate());
        booking.setTime(bookingDTO.getTime());
        booking.setConfirmed(false);
        return booking;
    }
}