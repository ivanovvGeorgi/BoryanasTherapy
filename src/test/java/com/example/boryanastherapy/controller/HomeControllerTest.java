package com.example.boryanastherapy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@WithMockUser
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userRole", is(false)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void homePageWithAdminRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userRole", is(true)));
    }

    @WithMockUser
    @Test
    void homePageWithRegularUserRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userRole", is(false))); // Regular user is not ADMIN
    }

    @Test
    void indexPageWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userRole", is(false)));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void indexPageWithAdminRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("userRole", is(true)));
    }

    @Test
    void confirmationPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/confirmation"))
                .andExpect(status().isOk())
                .andExpect(view().name("confirmation"));
    }

    @Test
    void aboutPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    void termsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/terms"))
                .andExpect(status().isOk())
                .andExpect(view().name("terms"));
    }

    @Test
    void privacyPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/privacy"))
                .andExpect(status().isOk())
                .andExpect(view().name("privacy"));
    }

    @Test
    void cookiesPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cookies"))
                .andExpect(status().isOk())
                .andExpect(view().name("cookies"));
    }
}