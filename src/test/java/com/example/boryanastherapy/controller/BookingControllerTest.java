package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.model.dto.BookingDTO;
import com.example.boryanastherapy.model.entity.Booking;
import com.example.boryanastherapy.model.entity.TimeSlot;
import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testShowBookingForm() throws Exception {
        // Arrange: mock available time slots for today
        LocalDate today = LocalDate.now();
        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(9, 0), true),
                new TimeSlot(LocalTime.of(10, 0), true)
        );
        when(bookingService.getAvailableSlotsForDay(today)).thenReturn(timeSlots);

        // Act & Assert: Send GET request and verify the booking form is displayed
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking-form"))
                .andExpect(model().attributeExists("bookingDTO"))
                .andExpect(model().attribute("date", today))
                .andExpect(model().attribute("timeSlots", timeSlots));
    }

    @Test
    void testSubmitBookingWithErrors() throws Exception {
        // Arrange: Create an invalid bookingDTO
        BookingDTO bookingDTO = new BookingDTO();  // Assume this is invalid
        when(bookingService.getAvailableSlotsForDay(bookingDTO.getDate())).thenReturn(List.of());

        // Act & Assert: Send POST request and expect to return the form with errors
        mockMvc.perform(post("/book")
                        .flashAttr("bookingDTO", bookingDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("booking-form"))
                .andExpect(model().attributeExists("timeSlots"))
                .andExpect(model().attributeHasFieldErrors("bookingDTO"));
    }

    @Test
    void testSubmitBookingWithSuccess() throws Exception {
        // Arrange: Create a BookingDTO with valid data
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setName("Client Name");  // Name is set here
        bookingDTO.setEmail("client@example.com");
        bookingDTO.setDate(LocalDate.of(2025, 4, 11));
        bookingDTO.setTime(LocalTime.of(9, 0));

        // Mock the service methods
        when(bookingService.isSlotAvailable(bookingDTO.getDate(), bookingDTO.getTime())).thenReturn(true);

        // Act: Perform the POST request to submit the booking
        mockMvc.perform(post("/book")
                        .flashAttr("bookingDTO", bookingDTO)) // Flash attributes for form submission
                .andExpect(status().is3xxRedirection()) // Expect redirect on success
                .andExpect(view().name("redirect:/confirmation")); // Redirect to confirmation page

        // Assert: Verify that the correct email sending happens with dynamic name
        verify(emailService).sendEmail(
                "client@example.com",
                "Appointment Confirmation!",
                "Hello Client Name! You have successfully booked an appointment for 2025-04-11 at 09:00. For any questions, please contact us. We are looking forward to seeing you!"
        );
        verify(emailService).sendEmail(
                "diffrent@abv.bg",
                "New Appointment!",
                "Client Name has made an appointment for 2025-04-11 at 09:00!"
        );
    }



    @Test
    void testSubmitBookingWithSlotNotAvailable() throws Exception {
        // Arrange: Create a bookingDTO and mock that the slot is unavailable
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setDate(LocalDate.now());
        bookingDTO.setTime(LocalTime.of(9, 0));
        bookingDTO.setEmail("client@example.com");
        bookingDTO.setName("Client");

        when(bookingService.isSlotAvailable(bookingDTO.getDate(), bookingDTO.getTime())).thenReturn(false);
        when(bookingService.getAvailableSlotsForDay(bookingDTO.getDate())).thenReturn(List.of(
                new TimeSlot(LocalTime.of(9, 0), false),
                new TimeSlot(LocalTime.of(10, 0), true)
        ));

        // Act & Assert: Send POST request and expect the error message
        mockMvc.perform(post("/book")
                        .flashAttr("bookingDTO", bookingDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("booking-form"))
                .andExpect(model().attribute("error", "The selected time slot is no longer available."));
    }

    @Test
    void testGetBookingsForDate() throws Exception {
        // Arrange: Mock the service to return bookings for a specific date
        LocalDate date = LocalDate.of(2025, 4, 12);
        List<Booking> bookings = List.of(new Booking());  // Assuming you return a list of bookings

        when(bookingService.getBookingsForDay(date)).thenReturn(bookings);

        // Act & Assert: Send GET request to check bookings for the date
        mockMvc.perform(get("/book/date").param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())  // Ensure the response is an array
                .andExpect(jsonPath("$[0]").exists());  // Check that the first booking exists
    }


    @Test
    public void testCheckAvailability() throws Exception {
        // Sample date and time for the test
        String dateStr = "2025-04-11";
        String timeStr = "10:00";

        // Mocking service behavior
        when(bookingService.isSlotAvailable(any(LocalDate.class), any(LocalTime.class))).thenReturn(true);

        // Perform the GET request to /book/check
        mockMvc.perform(get("/book/check?date=" + dateStr + "&time=" + timeStr))
                .andExpect(status().isOk()) // Expect 200 OK response
                .andExpect(content().string("true")); // Expect the response body to be "true"
    }




    @Test
    void testGetAvailableSlots() throws Exception {
        // Prepare sample TimeSlot data
        List<TimeSlot> availableSlots = List.of(
                new TimeSlot(LocalTime.of(9, 0), true),
                new TimeSlot(LocalTime.of(10, 0), false)
        );

        // Mock the service method that returns the available slots
        when(bookingService.getAvailableSlotsForDay(any(LocalDate.class))).thenReturn(availableSlots);

        // Act: Perform GET request to fetch available slots
        mockMvc.perform(get("/book/available-slots")
                        .param("date", "2025-04-11"))
                .andExpect(status().isOk())  // Expect a 200 OK status
                .andExpect(content().contentType("application/json"))  // Ensure content is JSON
                .andExpect(jsonPath("$[0].time").value("09:00"))  // Check first slot time
                .andExpect(jsonPath("$[0].isAvailable").value(true))  // Check first slot availability
                .andExpect(jsonPath("$[1].time").value("10:00"))  // Check second slot time
                .andExpect(jsonPath("$[1].isAvailable").value(false));  // Check second slot availability
    }




}
