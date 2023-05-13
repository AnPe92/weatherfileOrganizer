package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataFetcher {
    public String fetchWeatherData(String location) throws IOException {

        //Should not be stored like this! but for simplicity
        String apiKey = "&appid=3442d6235cbeda429fd19624f1a34b0f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=";

        URL api = new URL(url + location + apiKey);
        HttpURLConnection connection = (HttpURLConnection) api.openConnection();
        connection.setRequestMethod("GET");

        int resCode = connection.getResponseCode();
        if (resCode != HttpURLConnection.HTTP_OK) {
            return ("Something went wrong when trying to fetch: " + resCode + " please try another city");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the weather description
            JSONArray weatherArray = jsonResponse.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            String weatherDescription = weatherObject.getString("main");

            // Return the weather description
            return (weatherDescription);
        }
    }
}
