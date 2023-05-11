package org.example;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class FileOrganizer
{
    //Api key should not be kept like this, only doing it so the app will work with less setup!
    public static void main( String[] args ) throws IOException {


        Scanner scanner = new Scanner(System.in);
        WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();
        boolean running = true;
        while(running) {

            System.out.println("Hello Cool User!\nPlease enter a location:");
            String location = scanner.nextLine();
            if(location.equals("end")){
                running = false;
            }


            System.out.println(" --------------------- ");
            System.out.println(weatherDataFetcher.fetchWeatherData(location));
            System.out.println(" --------------------- ");
        }
        }
}
