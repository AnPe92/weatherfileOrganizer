package org.example;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@NoArgsConstructor
public class CreateConnection {
    public HttpURLConnection createConnection(String location) throws IOException {
        String apiKey = "&appid=3442d6235cbeda429fd19624f1a34b0f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=";

        URL connection = new URL(url + location + apiKey);
        return (HttpURLConnection) connection.openConnection();
    }
}
