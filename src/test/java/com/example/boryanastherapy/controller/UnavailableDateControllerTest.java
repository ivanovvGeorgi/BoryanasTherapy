package com.example.boryanastherapy.controller;

import com.example.boryanastherapy.service.BookingService;
import com.example.boryanastherapy.service.UnavailableDateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnavailableDateController.class)
@Import(UnavailableDateControllerTest.TestConfig.class)
public class UnavailableDateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UnavailableDateService unavailableDateService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BookingService bookingService() {
            return Mockito.mock(BookingService.class);
        }

        @Bean
        public UnavailableDateService unavailableDateService() {
            return Mockito.mock(UnavailableDateService.class);
        }

        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((authz) -> authz
                            .requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll()
                            .anyRequest().authenticated()
                    )
                    .httpBasic(withDefaults())
                    .formLogin(withDefaults());
            return http.build();
        }
    }

    @Test
    void getUnavailableDatesShouldReturnListOfDates() throws Exception {
        List<String> unavailableDates = Arrays.asList("2025-04-15", "2025-04-20");
        when(unavailableDateService.getAllUnavailableDates()).thenReturn(unavailableDates);

        mockMvc.perform(MockMvcRequestBuilders.get("/public/unavailable-dates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")) // Updated assertion
                .andExpect(jsonPath("$[0]", is("2025-04-15")))
                .andExpect(jsonPath("$[1]", is("2025-04-20")));
    }

    @Test
    void getUnavailableDatesShouldReturnEmptyListIfNoDates() throws Exception {
        when(unavailableDateService.getAllUnavailableDates()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/public/unavailable-dates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getFullyBookedDatesShouldReturnListOfDates() throws Exception {
        List<String> fullyBookedDates = Arrays.asList("2025-05-01", "2025-05-10");
        when(bookingService.getFullyBookedDates()).thenReturn(fullyBookedDates);

        mockMvc.perform(MockMvcRequestBuilders.get("/public/fully-booked-dates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0]", is("2025-05-01")))
                .andExpect(jsonPath("$[1]", is("2025-05-10")));
    }

    @Test
    void getFullyBookedDatesShouldReturnEmptyListIfNoDates() throws Exception {
        when(bookingService.getFullyBookedDates()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/public/fully-booked-dates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$").isEmpty());
    }
}