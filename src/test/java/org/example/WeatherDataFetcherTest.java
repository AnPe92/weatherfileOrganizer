package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherDataFetcherTest {
    @Mock
    private HttpURLConnection mockConnection;
    @Mock
    private CachedWeatherData mockCachedData;

    private WeatherDataFetcher weatherDataFetcher;
    private final String weatherDescription = "sunny";

    @BeforeEach
    void setUp() throws IOException {
        // Create a instance of weatherdatafetched with a mocked httpURLconnection and CachedWeatherData
        weatherDataFetcher = new WeatherDataFetcher(mockCachedData, mockConnection);

    }

    @Test
    void fetchWeatherDataTest() throws IOException {

        //
        String location = "osby";
        createMockResponse(true);

        Optional<WeatherData> weatherData = weatherDataFetcher.fetchWeatherData(location);

        assertTrue(weatherData.isPresent());
        assertEquals(location, weatherData.get().getLocation());
        assertEquals(weatherDescription, weatherData.get().getDescription());

    }

    @Test
    void badResponseTest() throws IOException {
        String location = "badResponse";

        createMockResponse(false);

        Optional<WeatherData> weatherData = weatherDataFetcher.fetchWeatherData(location);

        assertFalse(weatherData.isPresent());
    }

    //Create the response for the mocked weatherdatafetcher api call
    private void createMockResponse(boolean responseStatus) throws IOException {


        if (responseStatus) {
            String jsonResponse = "{\"weather\":[{\"main\":\"" + weatherDescription + "\" }]}";
            InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
            when(mockConnection.getInputStream()).thenReturn(inputStream);
        } else {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
            String jsonResponse = "{\"ERROR: BAD REQUEST}";
        }
    }
}
