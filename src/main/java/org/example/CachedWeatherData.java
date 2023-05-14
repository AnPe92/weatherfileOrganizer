package org.example;

import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class CachedWeatherData {
    Instant timeStamp;
    String description;

}
