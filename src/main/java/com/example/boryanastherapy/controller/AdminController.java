package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.Message;
import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.MessageService;
import com.example.boryanastherapy.service.UnavailableDateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BookingService bookingService;
    private final MessageService messageService;
    private final UnavailableDateService unavailableDateService;

    public AdminController(BookingService bookingService, MessageService messageService, UnavailableDateService unavailableDateService) {
        this.bookingService = bookingService;
        this.messageService = messageService;
        this.unavailableDateService = unavailableDateService;
    }
    @GetMapping("/login")
    public String adminLogin(){
        return "admin/login";
    }

    @GetMapping("/control-panel")
    public String adminDashboard(Model model) {
        // Provide links to both bookings and messages
        model.addAttribute("bookingsLink", "/admin/dashboard");
        model.addAttribute("messagesLink", "/admin/messages");
        return "admin/control-panel";  // Updated page name
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        // Get all bookings sorted by date and time
        List<Booking> bookings = bookingService.getAllBookingsSortedByDateAndTime();

        // Separate bookings into past and upcoming
        LocalDate today = LocalDate.now();

        // Filter past bookings
        List<Booking> pastBookings = bookings.stream()
                .filter(booking -> booking.getDate().isBefore(today) || (booking.getDate().isEqual(today) && booking.getTime().isBefore(LocalTime.now())))
                .collect(Collectors.toList());

        // Filter upcoming bookings
        List<Booking> upcomingBookings = bookings.stream()
                .filter(booking -> booking.getDate().isAfter(today) || (booking.getDate().isEqual(today) && booking.getTime().isAfter(LocalTime.now())))
                .collect(Collectors.toList());

        // Sort past bookings by date (reverse chronological) and then by hour
        pastBookings.sort(Comparator.comparing(Booking::getDate).reversed()
                .thenComparing(Booking::getTime));

        // Format LocalDate manually
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Booking booking : bookings) {
            String formattedDate = booking.getDate().format(formatter);
            booking.setFormattedDate(formattedDate); // Store the formatted date as a string
        }

        // Add both lists to the model
        model.addAttribute("pastBookings", pastBookings);
        model.addAttribute("upcomingBookings", upcomingBookings);
        return "admin/dashboard";
    }


    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/admin/dashboard";
    }
    @GetMapping("/messages")
    public String getMessages(Model model) {
        // Fetch all messages from the database
        List<Message> messages = messageService.getAllMessagesSortedByDate();

        // Separate the messages into new and read lists
        List<Message> newMessages = messages.stream()
                .filter(message -> !message.getIsRead()) // Assuming you have an 'isRead' flag
                .collect(Collectors.toList());

        List<Message> readMessages = messages.stream()
                .filter(Message::getIsRead)
                .collect(Collectors.toList());

        // Add messages to the model
        model.addAttribute("newMessages", newMessages);
        model.addAttribute("readMessages", readMessages);
        return "admin/messages";  // Return the new Thymeleaf template for messages
    }

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return "redirect:/admin/messages";  // Redirect back to the messages page
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);  // Delete the message by ID
        return "redirect:/admin/messages";  // Redirect back to the messages page
    }

    @GetMapping("/calendar")
    public String adminPanel() {
        return "admin/calendar";
    }

    @GetMapping("/unavailable-dates")
    @ResponseBody
    public List<String> getUnavailableDates() {
        return unavailableDateService.getAllUnavailableDates();
    }

    @PostMapping("/unavailable-dates")
    @ResponseBody
    public ResponseEntity<String> setUnavailableDates(@RequestBody List<String> dates) {
        unavailableDateService.saveUnavailableDates(dates);
        return ResponseEntity.ok("Unavailable dates saved successfully.");
    }
    @DeleteMapping("/unavailable-dates")
    public ResponseEntity<String> removeAllUnavailableDates() {
        try {
            unavailableDateService.removeAllUnavailableDates();
            return ResponseEntity.ok("All unavailable dates have been removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Could not remove unavailable dates.");
        }
    }

}
