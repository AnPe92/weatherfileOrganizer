package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WeatherDataFetcher {
    Scanner scanner = new Scanner(System.in);
    Map<String, CachedWeatherData> cachedData = new HashMap<>();

    public Map<String, String> fetchWeatherData(String location) throws IOException {

        Map<String, String> locationAndDescription = new HashMap<>();

        //Checks if location is in cache map and returns it if exists
        if (cachedData != null && cachedData.containsKey(location.toLowerCase()) && cachedData.get(location).timeStamp.isAfter(Instant.now().minusSeconds(600))) {
            locationAndDescription.put("location", location);
            locationAndDescription.put("description", cachedData.get(location).description);
            return locationAndDescription;
        } else {
            HttpURLConnection connection = null;
            boolean askAgain = true;
            while (askAgain) {
                //Shouldnt save api key here
                String apiKey = "&appid=3442d6235cbeda429fd19624f1a34b0f";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=";
                //Setup URL for get call and then make a get call on that URL
                URL api = new URL(url + location + apiKey);
                connection = (HttpURLConnection) api.openConnection();
                connection.setRequestMethod("GET");
                //Get response from api
                int resCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();
                //if response is not 200 ok, ask user for a new location, should be more error handling for different responses
                if (resCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect();
                    System.out.println("Something went wrong when trying to fetch: " + resCode);
                    System.out.println("response message: " + location + " " + responseMessage + " please try again");
                    location = scanner.nextLine();
                } else {
                    askAgain = false;
                }
            }
            //Get the json response and makes string from the input stream
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                //convert string to a jsonobject, could look up implementing GSON library to get json object directly
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract the weather description
                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String weatherDescription = weatherObject.getString("main");
                locationAndDescription.put("location", location);
                locationAndDescription.put("description", weatherDescription);

                //caches data for future use
                if (cachedData != null && location != null && weatherDescription != null) {
                    cachedData.put(location.toLowerCase(), new CachedWeatherData(Instant.now(), weatherDescription));
                }
                // Return the weather description
                return locationAndDescription;

            }
        }
    }
}
