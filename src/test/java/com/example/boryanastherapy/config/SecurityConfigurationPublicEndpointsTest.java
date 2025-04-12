package com.example.boryanastherapy.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigurationPublicEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @WithAnonymousUser
    @Test
    void publicEndpointRootShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointIndexHtmlShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointCssShouldReturnOkForExistingResource() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/css/styles.css"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointJsShouldReturnOkForExistingResource() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/js/booking.js"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointImagesShouldReturnOkForExistingResource() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/images/my_logo.png"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointBookShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointConfirmationShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/confirmation"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointContactShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contact"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointAboutShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointPrivacyShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/privacy"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointTermsShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/terms"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointCookiesShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cookies"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointSendMessageShouldReturnOk() throws Exception {
        // Assuming /send-message handles GET requests.
        // If it only handles POST, change this to MockMvcRequestBuilders.post("/send-message")
        mockMvc.perform(post("/send-message")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John")
                        .param("email", "john@example.com")
                        .param("message", "Hello, this is a test message."))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));

    }


    @WithAnonymousUser
    @Test
    void publicEndpointPublicShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/unavailable-dates"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointServicesShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services"))
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void publicEndpointServicesSubpageShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/services/vret"))
                .andExpect(status().isOk());
    }
}