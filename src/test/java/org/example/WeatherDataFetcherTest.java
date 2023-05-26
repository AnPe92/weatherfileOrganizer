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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherDataFetcherTest {
    @Mock
    private HttpURLConnection mockConnection;

    private WeatherDataFetcher weatherDataFetcher;
    private final String weatherDescription = "sunny";


    @BeforeEach
    void setUp() throws IOException {
        //Create a instance of weatherdatafetched with a mocked httpURLconnection
        weatherDataFetcher = new WeatherDataFetcher(mockConnection);
        //Setup the response to get from the api call
        createMockResponse();
    }

    @Test
    void fetchWeatherDataTest() throws IOException {

        String location = "osby";
        //Saving response from fetchweatherdata call
        Map<String, String> weatherData = weatherDataFetcher.fetchWeatherData(location, mockConnection);


        assertEquals(location, weatherData.get("location"));
        assertEquals(weatherDescription, weatherData.get("description"));

    }

    //Create the response for the mocked weatherdatafetcher api call
    private void createMockResponse() throws IOException {

        String jsonResponse = "{\"weather\":[{\"main\":\"" + weatherDescription + "\" }]}";
        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());

        when(mockConnection.getInputStream()).thenReturn(inputStream);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
    }
}
