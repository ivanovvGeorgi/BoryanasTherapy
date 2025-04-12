package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.MessageService;
import com.example.boryanastherapy.service.UnavailableDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private MessageService messageService;

    @Mock
    private UnavailableDateService unavailableDateService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testAdminLogin() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }

    @Test
    void testAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    void testCancelBooking() throws Exception {
        long bookingId = 1L;
        doNothing().when(bookingService).cancelBooking(bookingId);

        mockMvc.perform(post("/admin/cancel/{id}", bookingId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/dashboard"));

        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    @Test
    void testGetMessages() throws Exception {
        mockMvc.perform(get("/admin/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/messages"));
    }

    @Test
    void testMarkMessageAsRead() throws Exception {
        long messageId = 1L;
        doNothing().when(messageService).markAsRead(messageId);

        mockMvc.perform(post("/admin/read/{id}", messageId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/messages"));

        verify(messageService, times(1)).markAsRead(messageId);
    }

    @Test
    void testDeleteMessage() throws Exception {
        long messageId = 1L;
        doNothing().when(messageService).deleteMessage(messageId);

        mockMvc.perform(post("/admin/delete/{id}", messageId))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/messages"));

        verify(messageService, times(1)).deleteMessage(messageId);
    }

    @Test
    void testGetUnavailableDates() throws Exception {
        mockMvc.perform(get("/admin/unavailable-dates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void testSetUnavailableDates() throws Exception {
        // Simulate the request body with some dates
        String json = "[\"2025-04-10\", \"2025-04-15\"]";

        mockMvc.perform(post("/admin/unavailable-dates")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Unavailable dates saved successfully."));

        verify(unavailableDateService, times(1)).saveUnavailableDates(anyList());
    }

    @Test
    void testRemoveAllUnavailableDates() throws Exception {
        doNothing().when(unavailableDateService).removeAllUnavailableDates();

        mockMvc.perform(delete("/admin/unavailable-dates"))
                .andExpect(status().isOk())
                .andExpect(content().string("All unavailable dates have been removed successfully."));

        verify(unavailableDateService, times(1)).removeAllUnavailableDates();
    }
}
