package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.TimeSlot;
import com.example.boryanastherapy.repository.BookingRepository;
import com.example.boryanastherapy.service.BookingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final int TOTAL_SLOTS_PER_DAY = 9;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void createBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsForDay(LocalDate date) {
        return bookingRepository.findByDate(date);
    }
    public List<TimeSlot> getAvailableSlotsForDay(LocalDate date) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);  // Start time: 9 AM
        LocalTime endTime = LocalTime.of(18, 0);  // End time: 6 PM

        // Generate all hourly time slots from 9 AM to 6 PM
        while (startTime.isBefore(endTime)) {
            boolean isAvailable = isSlotAvailable(date, startTime);
            timeSlots.add(new TimeSlot(startTime, isAvailable));  // Add time slot with availability status
            startTime = startTime.plusHours(1);  // Move to the next hour
        }

        return timeSlots;
    }

    @Override
    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllBookingsSortedByDateAndTime() {
        return bookingRepository.findAll(Sort.by(Sort.Order.asc("date"), Sort.Order.asc("time")));
    }

    public boolean isFullyBooked(LocalDate date) {
        // Count the number of booked slots for the given date
        long bookedSlots = bookingRepository.countByDate(date);

        // If booked slots equal total slots, the day is fully booked
        return bookedSlots >= TOTAL_SLOTS_PER_DAY;
    }

    public List<String> getFullyBookedDates() {
        List<LocalDate> bookedDates = bookingRepository.findBookedDates();
        return bookedDates.stream()
                .filter(this::isFullyBooked) // Only keep fully booked dates
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSlotAvailable(LocalDate date, LocalTime time) {
        return bookingRepository.findByDateAndTime(date, time).isEmpty();
    }
}
