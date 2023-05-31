package org.example;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.util.Optional;


@RequiredArgsConstructor
public class WeatherDataFetcher {
    private final CachedWeatherData cachedData;
    private final CreateConnection createConnection;

    public Optional<WeatherData> fetchWeatherData(String location) throws IOException {

        try {
            return checkCachedWeatherData(location);
        } catch (IOException e) {
            //returning optional so that application not just terminates

            System.out.println("Error fetching :" + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<WeatherData> checkCachedWeatherData(String location) throws IOException {
        WeatherData cachedWeatherData = cachedData.get(location);
        if (cachedWeatherData != null && cachedWeatherData.getTime().isAfter(Instant.now().minusSeconds(600))) {
            return Optional.of(cachedWeatherData);
        } else {
            return Optional.of(fetchData(location));
        }
    }


    private WeatherData fetchData(String location) throws IOException {
        System.out.println("ENTERING GETHCH DATA");

        HttpURLConnection connection = createConnection.createConnection(location);

        connection.setRequestMethod("GET");

        int resCode = connection.getResponseCode();
        try {
            if (resCode != HttpURLConnection.HTTP_OK) {
                connection.disconnect();
                throw new IOException("Failed to fetch data: " + resCode);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String weatherDescription = weatherObject.getString("main");

                WeatherData weatherData = new WeatherData(location, weatherDescription, Instant.now());

                cachedData.put(location.toLowerCase(), weatherData);

                return weatherData;
            }
        } finally {
            connection.disconnect();
        }
    }
}
