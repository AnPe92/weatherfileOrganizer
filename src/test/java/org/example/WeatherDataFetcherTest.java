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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        // Setup the response to get from the api call
        createMockResponse();
    }

    @Test
    void fetchWeatherDataTest() throws IOException {

        String location = "osby";
        //Saving response from fetchweatherdata call
        WeatherData weatherData = weatherDataFetcher.fetchWeatherData(location);

        assertEquals(location, weatherData.getLocation());
        assertEquals(weatherDescription, weatherData.getDescription());

    }

    //Create the response for the mocked weatherdatafetcher api call
    private void createMockResponse() throws IOException {

        String jsonResponse = "{\"weather\":[{\"main\":\"" + weatherDescription + "\" }]}";
        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

        when(mockConnection.getInputStream()).thenReturn(inputStream);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
    }
}
