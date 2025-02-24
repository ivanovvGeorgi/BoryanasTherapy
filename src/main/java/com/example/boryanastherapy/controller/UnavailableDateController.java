package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.UnavailableDateService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/public") // Public endpoint
public class UnavailableDateController {

    private final BookingService bookingService;
    private final UnavailableDateService unavailableDateService;

    public UnavailableDateController(BookingService bookingService, UnavailableDateService unavailableDateService) {
        this.bookingService = bookingService;
        this.unavailableDateService = unavailableDateService;
    }

    // ✅ Allow anonymous users to get unavailable dates
    @GetMapping("/unavailable-dates")
    public List<String> getUnavailableDates() {
        return unavailableDateService.getAllUnavailableDates();
    }

    @GetMapping("/fully-booked-dates")
    public List<String> getFullyBookedDates() {
        return bookingService.getFullyBookedDates();
    }
}
