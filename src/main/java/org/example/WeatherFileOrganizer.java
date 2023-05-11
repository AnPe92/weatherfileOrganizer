package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class WeatherFileOrganizer {

    //Instanciating classes that will be used

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileOrganizer fileOrganizer = new FileOrganizer();
        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();


        boolean running = true;
        while (running) {
            System.out.println("Hello Cool User!\nTime to sort some files!");
            System.out.println("Choose what you want to do: \n1: sort files\n2: reset sorted files");
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "1":
                    System.out.println("Enter a PATH:");
                    String pathInput = scanner.nextLine();
                    System.out.println("Enter a LOCATION:");
                    String weatherInput = scanner.nextLine();
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

