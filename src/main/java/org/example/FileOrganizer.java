package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOrganizer {
    Scanner scanner = new Scanner(System.in);

    public int countFilesInDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        long numFiles = 0;
        try {
            numFiles = Files.list(path).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return (int) numFiles;
    }

    public void createDirectory(String directoryPath, String name) throws IOException {
        Path newDirectory = Paths.get(directoryPath, name);
        Files.createDirectory(newDirectory);
    }

    public List<String> getFileName(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            try (Stream<Path> paths = Files.list(path)) {
                paths.filter(Files::isRegularFile)
                        .map(p -> p.getFileName().toString())
                        .forEach(fileList::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileList;
    }

    public void moveFiles(String sourceDirectoryPath, String weather, String WeatherLocation) throws IOException {
        List<String> fileNames = getFileName(sourceDirectoryPath);
        int folderCount = 0;
        for (String fileName : fileNames) {
            String fileExtension = getFileExtension(fileName);
            Path sourceFilePath = Paths.get(sourceDirectoryPath, fileName);
            Path targetDirectoryPath = Paths.get(sourceDirectoryPath, weather + "_" + fileExtension.substring(1));
            Path targetFilePath = targetDirectoryPath.resolve(fileName);

            // Create the target directory if it does not exist
            if (!Files.exists(targetDirectoryPath)) {
                Files.createDirectory(targetDirectoryPath);
                folderCount++;
            }
            // Handle file name conflicts
            String baseFileName = fileName.substring(0, fileName.lastIndexOf("."));
            int counter = 1;
            while (Files.exists(targetFilePath)) {
                targetFilePath = targetDirectoryPath.resolve(baseFileName + "(" + counter + ")" + fileExtension);
                counter++;
            }

            // Move the file
            Files.move(sourceFilePath, targetFilePath);
        }
        System.out.println(" --------------------- ");
        System.out.println("Number of files moved: " + fileNames.size());
        System.out.println("Number of subdirectories made: " + folderCount);
        System.out.println("The weather is: " + weather + " in " + WeatherLocation);
        System.out.println(" --------------------- ");

    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }


    public void resetFiles() throws IOException {

        System.out.println("Enter a path to reset folder: ");
        String userInput = scanner.nextLine();

        Path path = Paths.get(userInput);

        Stream<Path> folders = Files.walk(path);
        folders.filter(Files::isRegularFile).forEach(file -> {

            try {
                moveFileWithNameChange(file, path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteEmptyFolders(path);
    }

    public void moveFileWithNameChange(Path filePath, Path targetLocation) throws IOException {
        String newName = filePath.getFileName().toString();
        Path checkTargetLocation = targetLocation.resolve(newName);
        int counter = 0;
        if (filePath.getParent().equals(targetLocation)) {
        } else {

            while (true) {
                if (Files.exists(checkTargetLocation)) {
                    counter++;
                    newName = " COPY " + counter + filePath.getFileName();
                    checkTargetLocation = targetLocation.resolve(newName);
                } else {
                    Files.move(filePath, targetLocation.resolve(newName));
                    break;
                }
            }
        }
    }

    public void deleteEmptyFolders(Path filePath) throws IOException {
        List<Path> directories = Files.walk(filePath).filter(Files::isDirectory).collect(Collectors.toList());
        directories.forEach(directory ->
        {
            try {
                checkIfEmpty(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void checkIfEmpty(Path directory) throws IOException {
        int check = Files.walk(directory).collect(Collectors.toList()).size();
        if (check <= 1)
            Files.delete(directory);
    }
}

//        String userInput;
//        Path path;
//        while (true) {
//            System.out.println("Enter the path for folder to reset: ");
//            userInput = scanner.nextLine();
//            Path inputPath = Paths.get(userInput);
//
//            if (Files.exists(inputPath)) {
//                path = inputPath;
//                break;
//            } else {
//                System.out.println("Invalid path. Please try again.");
//            }
//        }
//        try (Stream<Path> paths = Files.walk(path)) {
//            paths.filter(Files::isRegularFile).forEach(file -> {
//                try {
//                    Files.move(file, path.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
//                } catch (IOException e) {
//                    throw new UncheckedIOException(e);
//                }
//            });
//        }
//        try (Stream<Path> paths = Files.walk(path)) {
//            paths.filter(Files::isDirectory)
//                    .sorted(Comparator.reverseOrder())
//                    .forEach(dir -> {
//                        try {
//                            if (!dir.equals(path)) { // don't delete the original directory
//                                Files.delete(dir);
//                            }
//                        } catch (IOException e) {
//                            throw new UncheckedIOException(e);
//                        }
//                    });
//        }
