package com.example.boryanastherapy.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    @JsonFormat(pattern = "HH:mm")  // Ensures LocalTime is serialized as a string in HH:mm format
    private LocalTime time;
    private boolean isAvailable;
}
