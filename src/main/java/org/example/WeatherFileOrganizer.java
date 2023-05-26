package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class WeatherFileOrganizer {

    //Instanciating classes that will be used

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Cool User!\nTime to sort some files!");
        Scanner scanner = new Scanner(System.in);
        FileOrganizer fileOrganizer = new FileOrganizer();
        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();
        CreateConnection connection = new CreateConnection();

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
                    System.out.println("Enter a LOCATION:");
                    weatherInput = scanner.nextLine();
                    Map<String, String> fetchedWeatherData = weatherDataFetcher.fetchWeatherData(weatherInput, connection.createConnection(weatherInput));
                    String fileData = fileOrganizer.moveFiles(pathInput, fetchedWeatherData.get("description"));
                    File directory = new File(pathInput);
                    Desktop.getDesktop().open(directory);
                    System.out.println("The weather in " + fetchedWeatherData.get("location") + " is: " + fetchedWeatherData.get("description"));
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

