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
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherDataFetcherTest {
    @Mock
    private HttpURLConnection mockConnection;
    @Mock
    private CachedWeatherData mockCachedData;
    @Mock
    private CreateConnection mockCreateConnection;

    private WeatherDataFetcher weatherDataFetcher;
    private final String weatherDescription = "sunny";

    @BeforeEach
    void setUp() throws IOException {
        when(mockCreateConnection.createConnection(anyString()))
                .thenReturn(mockConnection);
        weatherDataFetcher = new WeatherDataFetcher(mockCachedData, mockCreateConnection);
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

        assertEquals(400, mockConnection.getResponseCode());
    }

    @Test
    void fetchCachedDataTest() throws IOException {
        String location = "osby";
        createMockResponse(true);

        // Call fetchWeatherData for the first time. This should trigger an API call.
        Optional<WeatherData> weatherData1 = weatherDataFetcher.fetchWeatherData(location);

        // Make sure data in object is as expected
        assertTrue(weatherData1.isPresent());
        assertEquals(location, weatherData1.get().getLocation());
        assertEquals(weatherDescription, weatherData1.get().getDescription());

        // Prepare a mock WeatherData object with a timestamp that is within the 10-minute limit.
        WeatherData mockWeatherData = new WeatherData(location, weatherDescription, Instant.now());

        // When fetchWeatherData is called again, CachedWeatherData should return the mockWeatherData object.
        when(mockCachedData.get(location.toLowerCase())).thenReturn(mockWeatherData);

        // Call fetchWeatherData for the second time with the same location.
        Optional<WeatherData> weatherData2 = weatherDataFetcher.fetchWeatherData(location);

        // Make sure that second weatherdata2 has expected data
        assertTrue(weatherData2.isPresent());
        assertEquals(location, weatherData2.get().getLocation());
        assertEquals(weatherDescription, weatherData2.get().getDescription());

        // Verify that only one api call was made.
        verify(mockConnection, times(1)).getResponseCode();

        // Verify that the data was correctly stored in the cache after the first call
        verify(mockCachedData, times(1)).put(eq(location.toLowerCase()), any(WeatherData.class));

        // Check that cachedData has been used for both calls, since the logic checks cache then if no data makes a api call
        verify(mockCachedData, times(2)).get(location.toLowerCase());
    }


    //Create the response for the mocked weatherdatafetcher api call
    private void createMockResponse(boolean responseStatus) throws IOException {
        InputStream inputStream;
        if (responseStatus) {
            String jsonResponse = "{\"weather\":[{\"main\":\"" + weatherDescription + "\" }]}";
            inputStream = new ByteArrayInputStream(jsonResponse.getBytes());
            when(mockConnection.getInputStream()).thenReturn(inputStream);
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        } else {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        }
    }
}
