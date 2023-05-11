package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class WeatherFileOrganizer {

    //Instanciating classes that will be used

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileOrganizer fileOrganizer = new FileOrganizer();
        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();

        String pathInput;
        String weatherInput;
        boolean running = true;
        while (running) {
            System.out.println("Hello Cool User!\nTime to sort some files!");
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

                    String fetchedWeatherData = weatherDataFetcher.fetchWeatherData(weatherInput);
                    System.out.println(fetchedWeatherData + " <----------------");
                    //runnning methods
                    fileOrganizer.moveFiles(pathInput, fetchedWeatherData);
                    File directory = new File(pathInput);
                    Desktop.getDesktop().open(directory);
                    break;
                case "2":
                    fileOrganizer.resetFiles();
                    break;
                case "exit":
                    running = false;
                    break;
            }
        }
    }
}

