package org.example;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@NoArgsConstructor
public class CreateConnection {
    //Creating a connection to the api
    public HttpURLConnection createConnection(String location) throws IOException {
        System.out.println("Entering createConnection()");
        String apiKey = "&appid=3442d6235cbeda429fd19624f1a34b0f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + location + apiKey;

        System.out.println(url);
        URL connection = new URL(url);
        return (HttpURLConnection) connection.openConnection();
    }
}
