package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.UnavailableDate;
import com.example.boryanastherapy.repository.UnavailableDateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnavailableDateServiceImplTest {

    @Mock
    private UnavailableDateRepository unavailableDateRepository;  

    @InjectMocks
    private UnavailableDateServiceImpl unavailableDateService;  

    private UnavailableDate unavailableDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  
        unavailableDate = new UnavailableDate("2025-04-15");
    }

    @Test
    void testGetAllUnavailableDates() {
        
        when(unavailableDateRepository.findAll()).thenReturn(List.of(unavailableDate));

        List<String> result = unavailableDateService.getAllUnavailableDates();

        
        assertEquals(List.of("2025-04-15"), result);
        verify(unavailableDateRepository, times(1)).findAll();  
    }

    @Test
    void testSaveUnavailableDates() {
        List<String> dates = List.of("2025-04-16", "2025-04-17");

        
        unavailableDateService.saveUnavailableDates(dates);

        
        verify(unavailableDateRepository, times(1)).deleteAll();  
        verify(unavailableDateRepository, times(1)).saveAll(anyList());  
    }

    @Test
    void testRemoveAllUnavailableDates() {
        
        unavailableDateService.removeAllUnavailableDates();

        
        verify(unavailableDateRepository, times(1)).deleteAll();
    }
}
