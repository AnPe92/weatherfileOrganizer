package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Data
@AllArgsConstructor
@Setter
public class WeatherData {
    private final String location;
    private final String description;
    private final Instant time;
}
