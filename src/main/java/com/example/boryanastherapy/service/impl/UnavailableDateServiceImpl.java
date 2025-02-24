package com.example.boryanastherapy.service.impl;

import com.example.boryanastherapy.model.entity.UnavailableDate;
import com.example.boryanastherapy.repository.UnavailableDateRepository;
import com.example.boryanastherapy.service.UnavailableDateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnavailableDateServiceImpl implements UnavailableDateService {

    private final UnavailableDateRepository unavailableDateRepository;


    public UnavailableDateServiceImpl(UnavailableDateRepository unavailableDateRepository) {
        this.unavailableDateRepository = unavailableDateRepository;
    }


    // ✅ Get all unavailable dates from the database
    public List<String> getAllUnavailableDates() {
        return unavailableDateRepository.findAll().stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
    }

    // ✅ Save new unavailable dates
    public void saveUnavailableDates(List<String> dates) {
        unavailableDateRepository.deleteAll();
        List<UnavailableDate> unavailableDates = dates.stream()
                .map(UnavailableDate::new)
                .collect(Collectors.toList());
        unavailableDateRepository.saveAll(unavailableDates);
    }

    @Override
    public void removeAllUnavailableDates() {
        unavailableDateRepository.deleteAll();
    }
}
