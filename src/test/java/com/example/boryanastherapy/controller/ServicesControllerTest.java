package com.example.boryanastherapy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ServicesController.class)
@WithMockUser
public class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void servicesPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("/services"));
    }

    @Test
    void individualCounsellingPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services/counselling"))
                .andExpect(status().isOk())
                .andExpect(view().name("/counselling"));
    }

    @Test
    void vretPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services/vret"))
                .andExpect(status().isOk())
                .andExpect(view().name("/vret"));
    }
}