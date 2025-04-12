package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.dto.BookingDTO;
import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.TimeSlot;
import com.example.boryanastherapy.model.mapper.BookingMapper;
import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.boryanastherapy.model.enums.Constants.*;

@Controller
@RequestMapping("/book")
public class BookingController {

    private final BookingService bookingService;
    private final EmailService emailService;

    @Autowired
    public BookingController(BookingService bookingService, EmailService emailService) {
        this.bookingService = bookingService;
        this.emailService = emailService;
    }

    // Show the booking form
    @GetMapping()
    public String showBookingForm(Model model) {
        model.addAttribute("bookingDTO", new BookingDTO());

        LocalDate today = LocalDate.now();
        List<TimeSlot> timeSlots = bookingService.getAvailableSlotsForDay(today);
        model.addAttribute("date", today);
        model.addAttribute("timeSlots", timeSlots);
        return "booking-form";
    }

    // Submit the booking form
    @PostMapping
    public String submitBooking(@Valid @ModelAttribute("bookingDTO") BookingDTO bookingDTO,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("timeSlots", bookingService.getAvailableSlotsForDay(bookingDTO.getDate()));
            return "booking-form"; // Return to the form if validation fails
        }

        boolean isAvailable = bookingService.isSlotAvailable(bookingDTO.getDate(), bookingDTO.getTime());

        if (isAvailable) {
            // Convert BookingDTO to Booking entity
            Booking booking = BookingMapper.toEntity(bookingDTO);
            bookingService.createBooking(booking);

            String clientEmail = bookingDTO.getEmail();
            String clientName = bookingDTO.getName();
            LocalDate date = bookingDTO.getDate();
            LocalTime time = bookingDTO.getTime();

            // Send confirmation to the client
            String text = String.format(SUCCESSFUL_BOOKING_FORMAT, clientName, date, time);
            emailService.sendEmail(clientEmail, CLIENT_SUBJECT, text);

            // Send booking details to the owner
            emailService.sendEmail(
                    OWNER_EMAIL,
                    OWNER_SUBJECT,
                    String.format(OWNER_TEXT_FORMAT, clientName, date, time)
            );

            // Redirect to confirmation page
            return "redirect:/confirmation";
        } else {
            model.addAttribute("error", "The selected time slot is no longer available.");
            model.addAttribute("timeSlots", bookingService.getAvailableSlotsForDay(bookingDTO.getDate()));
            return "booking-form"; // Reload the form with error message
        }
    }


    // Get bookings for a specific date (useful for availability checks)
    @GetMapping("/date")
    @ResponseBody
    public List<Booking> getBookingsForDate(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return bookingService.getBookingsForDay(date);
    }

    // Check if a specific slot is available (useful for frontend availability checks)
    @GetMapping("/check")
    @ResponseBody
    public boolean checkAvailability(@RequestParam("date") String dateStr, @RequestParam("time") String timeStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        return bookingService.isSlotAvailable(date, time);
    }
    @GetMapping("/available-slots")
    @ResponseBody
    public List<TimeSlot> getAvailableSlots(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);  // Parse the date
        return bookingService.getAvailableSlotsForDay(date);
    }
}
