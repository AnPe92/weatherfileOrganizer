package org.example;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
public class CachedWeatherData {

    Instant timeStamp;
    String description;
    private final Map<String, WeatherData> cachedData = new HashMap<>();

    public WeatherData get(String location) {
        return cachedData.get(location);
    }

    public void put(String location, WeatherData data) {
        cachedData.put(location, data);
    }
}
