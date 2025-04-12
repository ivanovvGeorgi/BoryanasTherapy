package com.example.boryanastherapy.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("time")
    private LocalTime time;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
}
