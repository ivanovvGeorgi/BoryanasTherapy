package com.example.boryanastherapy.service;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.boryanastherapy.model.enums.Constants.REMINDER_EMAIL_FORMAT;

@Service
public class BookingReminderService {

    private final EmailService emailService;

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingReminderService(EmailService emailService, BookingRepository bookingRepository) {
        this.emailService = emailService;
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void sendReminderEmails() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(24);

        // Get the target date and time for 24 hours from now
        LocalDate targetDate = targetTime.toLocalDate();
        LocalTime targetTimeOfDay = targetTime.toLocalTime();

        // Find bookings that match the exact date and time (24 hours from now)
        List<Booking> upcomingBookings = bookingRepository.findBookingsByExactDateAndTime(targetDate, targetTimeOfDay);

        for (Booking booking : upcomingBookings) {
            sendReminderEmail(booking);
        }
    }

    private void sendReminderEmail(Booking booking) {
        String to = booking.getEmail();
        String subject = "Booking Reminder";


        // Use your existing EmailService to send the email
        emailService.sendEmail(
                to,
                subject,
                String.format(REMINDER_EMAIL_FORMAT, booking.getDate(), booking.getDate()));
    }
}
