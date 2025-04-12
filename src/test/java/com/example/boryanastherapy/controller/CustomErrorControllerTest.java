package com.example.boryanastherapy.controller;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomErrorController.class)
@WithMockUser
public class CustomErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleError_404NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "Not Found")
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/some-nonexistent-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorCode", 404))
                .andExpect(model().attribute("errorMessage", "The page you are looking for does not exist."))
                .andExpect(model().attribute("path", "/some-nonexistent-page"));
    }

    @Test
    void handleError_403Forbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403)
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "Forbidden")
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/some-protected-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"))
                .andExpect(model().attribute("errorCode", 403))
                .andExpect(model().attribute("errorMessage", "You don't have permission to access this page."))
                .andExpect(model().attribute("path", "/some-protected-page"));
    }

    @Test
    void handleError_500InternalServerError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "Internal Server Error")
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/some-broken-endpoint"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/500"))
                .andExpect(model().attribute("errorCode", 500))
                .andExpect(model().attribute("errorMessage", "Internal server error occurred."))
                .andExpect(model().attribute("path", "/some-broken-endpoint"));
    }

    @Test
    void handleError_otherErrorWithStatusAndMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 418)
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "I'm a teapot")
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/some-weird-request"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorCode", 418))
                .andExpect(model().attribute("errorMessage", "I'm a teapot"))
                .andExpect(model().attribute("path", "/some-weird-request"));
    }

    @Test
    void handleError_otherErrorWithStatusNoMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 503)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/some-unavailable-service"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorCode", 503))
                .andExpect(model().attribute("errorMessage", "An unexpected error occurred"))
                .andExpect(model().attribute("path", "/some-unavailable-service"));
    }

    @Test
    void handleError_noStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "Something went wrong")
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorCode", "Unknown"))
                .andExpect(model().attribute("errorMessage", "An unexpected error occurred"))
                .andExpect(model().attribute("path", "/"));
    }
}