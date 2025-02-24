package com.example.boryanastherapy.service;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    void createBooking(Booking booking);

    List<Booking> getAllBookings();

    List<Booking> getBookingsForDay(LocalDate date);

    boolean isSlotAvailable(LocalDate date, LocalTime time);

    List<TimeSlot> getAvailableSlotsForDay(LocalDate date);

    void cancelBooking(Long id);

    List<Booking> getAllBookingsSortedByDateAndTime();

    boolean isFullyBooked(LocalDate date);

    List<String> getFullyBookedDates();
}
