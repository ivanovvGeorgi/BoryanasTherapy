package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.TimeSlot;
import com.example.boryanastherapy.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        Booking booking = new Booking();
        bookingService.createBooking(booking);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testGetAllBookings() {
        List<Booking> expectedBookings = List.of(new Booking(), new Booking());
        when(bookingRepository.findAll()).thenReturn(expectedBookings);
        List<Booking> result = bookingService.getAllBookings();
        assertThat(result).hasSize(2);
    }

    @Test
    void testGetBookingsForDay() {
        LocalDate date = LocalDate.now();
        List<Booking> expected = List.of(new Booking());
        when(bookingRepository.findByDate(date)).thenReturn(expected);
        assertThat(bookingService.getBookingsForDay(date)).isEqualTo(expected);
    }

    @Test
    void testGetAvailableSlotsForDay() {
        LocalDate date = LocalDate.now();
        when(bookingRepository.findByDateAndTime(eq(date), any(LocalTime.class))).thenReturn(Optional.empty());
        List<TimeSlot> slots = bookingService.getAvailableSlotsForDay(date);
        assertThat(slots).hasSize(9);
        assertThat(slots).allMatch(TimeSlot::isAvailable);
    }

    @Test
    void testCancelBooking() {
        bookingService.cancelBooking(1L);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void testIsFullyBooked_whenNotFull() {
        when(bookingRepository.countByDate(any())).thenReturn(5L);
        assertThat(bookingService.isFullyBooked(LocalDate.now())).isFalse();
    }

    @Test
    void testIsFullyBooked_whenFull() {
        when(bookingRepository.countByDate(any())).thenReturn(9L);
        assertThat(bookingService.isFullyBooked(LocalDate.now())).isTrue();
    }

    @Test
    void testGetFullyBookedDates() {
        LocalDate fullDate = LocalDate.of(2025, 4, 14);
        LocalDate notFullDate = LocalDate.of(2025, 4, 15);
        when(bookingRepository.findBookedDates()).thenReturn(List.of(fullDate, notFullDate));
        when(bookingRepository.countByDate(fullDate)).thenReturn(9L);
        when(bookingRepository.countByDate(notFullDate)).thenReturn(5L);

        List<String> result = bookingService.getFullyBookedDates();
        assertThat(result).containsExactly(fullDate.toString());
    }

    @Test
    void testIsSlotAvailable_whenAvailable() {
        when(bookingRepository.findByDateAndTime(eq(LocalDate.now()), eq(LocalTime.of(10, 0))))
                .thenReturn(Optional.empty());

        boolean isAvailable = bookingService.isSlotAvailable(LocalDate.now(), LocalTime.of(10, 0));

        assertTrue(isAvailable);
    }

    @Test
    void testIsSlotAvailable_whenTaken() {
        when(bookingRepository.findByDateAndTime(eq(LocalDate.now()), eq(LocalTime.of(10, 0))))
                .thenReturn(Optional.of(new Booking()));

        boolean isAvailable = bookingService.isSlotAvailable(LocalDate.now(), LocalTime.of(10, 0));

        assertFalse(isAvailable);
    }

}
