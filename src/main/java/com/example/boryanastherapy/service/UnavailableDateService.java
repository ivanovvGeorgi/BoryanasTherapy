package com.example.boryanastherapy.service;

import java.util.List;

public interface UnavailableDateService {
    List<String> getAllUnavailableDates();
    void saveUnavailableDates(List<String> dates);

    void removeAllUnavailableDates();
}
