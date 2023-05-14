package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherDataFetcher {
    Scanner scanner = new Scanner(System.in);

    public Map<String, String> fetchWeatherData(String location) throws IOException {

        //Should not be stored like this! but for simplicity
        HttpURLConnection connection = null;
        boolean askAgain = true;
        while (askAgain) {
            String apiKey = "&appid=3442d6235cbeda429fd19624f1a34b0f";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=";

            URL api = new URL(url + location + apiKey);
            connection = (HttpURLConnection) api.openConnection();
            connection.setRequestMethod("GET");

            int resCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            if (resCode != HttpURLConnection.HTTP_OK) {
                connection.disconnect();
                System.out.println("Something went wrong when trying to fetch: " + resCode);
                System.out.println("response message: " + location + " " + responseMessage + " please try again");
                location = scanner.nextLine();
            } else {
                askAgain = false;
            }

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
            Map<String, String> locationAndDescription = new HashMap<String, String>();
            locationAndDescription.put("location", location);
            locationAndDescription.put("description", weatherDescription);

            // Return the weather description
            return locationAndDescription;
        }
    }
}
