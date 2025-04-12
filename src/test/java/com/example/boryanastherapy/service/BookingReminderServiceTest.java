package com.example.boryanastherapy.service;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BookingReminderServiceTest {

    private EmailService emailService;
    private BookingRepository bookingRepository;
    private BookingReminderService bookingReminderService;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        bookingRepository = mock(BookingRepository.class);
        bookingReminderService = new BookingReminderService(emailService, bookingRepository);
    }

    @Test
    void shouldSendReminderEmailsForUpcomingBookings() {
        Booking booking1 = new Booking();
        booking1.setEmail("john@example.com");
        booking1.setDate(LocalDate.of(2025, 4, 13));
        booking1.setTime(LocalTime.of(15, 0));

        Booking booking2 = new Booking();
        booking2.setEmail("jane@example.com");
        booking2.setDate(LocalDate.of(2025, 4, 13));
        booking2.setTime(LocalTime.of(16, 0));

        when(bookingRepository.findBookingsWithin24Hours(
                any(LocalDate.class),
                any(LocalTime.class),
                any(LocalDate.class),
                any(LocalTime.class)
        )).thenReturn(List.of(booking1, booking2));

        bookingReminderService.sendReminderEmails();

        verify(bookingRepository, times(1)).findBookingsWithin24Hours(
                any(LocalDate.class),
                any(LocalTime.class),
                any(LocalDate.class),
                any(LocalTime.class)
        );

        verify(emailService, times(2)).sendEmail(anyString(), eq("Booking Reminder"), anyString());

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(2)).sendEmail(emailCaptor.capture(), anyString(), anyString());
        List<String> sentTo = emailCaptor.getAllValues();
        assertTrue(sentTo.contains("john@example.com"));
        assertTrue(sentTo.contains("jane@example.com"));
    }


    @Test
    void shouldNotSendEmailsIfNoUpcomingBookings() {
        when(bookingRepository.findBookingsWithin24Hours(any(), any(), any(), any()))
                .thenReturn(List.of());

        bookingReminderService.sendReminderEmails();

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
