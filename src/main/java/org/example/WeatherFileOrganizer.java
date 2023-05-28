package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class WeatherFileOrganizer {

    //Instanciating classes that will be used

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Cool User!\nTime to sort some files!");
        Scanner scanner = new Scanner(System.in);
        FileOrganizer fileOrganizer = new FileOrganizer();
        CreateConnection connection = new CreateConnection();
        CachedWeatherData cachedData = new CachedWeatherData(); // Create cached data
        // Create connection
        String pathInput;
        String weatherInput;


        boolean running = true;
        while (running) {
            System.out.println("Choose what you want to do: \n1: sort files\n2: reset sorted files");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    while (true) {
                        System.out.println("Enter a PATH:");
                        pathInput = scanner.nextLine();
                        Path path = Paths.get(pathInput);
                        if (Files.exists(path)) {
                            break;
                        }
                    }

                    Optional<WeatherData> fetchedWeatherData;
                    do {
                        System.out.println("Enter a LOCATION:");
                        weatherInput = scanner.nextLine();
                        HttpURLConnection httpConnection = connection.createConnection(weatherInput);
                        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher(cachedData, httpConnection);
                        fetchedWeatherData = weatherDataFetcher.fetchWeatherData(weatherInput);
                    } while (!fetchedWeatherData.isPresent());

                    String fileData = fileOrganizer.moveFiles(pathInput, fetchedWeatherData.get().getDescription());
                    File directory = new File(pathInput);
                    Desktop.getDesktop().open(directory);
                    System.out.println("The weather in " + fetchedWeatherData.get().getLocation() + " is: " + fetchedWeatherData.get().getDescription());
                    System.out.println(fileData);
                    break;
                case "2":
                    System.out.println("Enter a path to reset folder: ");
                    pathInput = scanner.nextLine();
                    fileOrganizer.resetFiles(pathInput);
                    break;
                case "exit":
                    running = false;
                    break;
            }
        }
    }
}

