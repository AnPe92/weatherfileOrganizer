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

    //Method counts number of files in a given path
    public int countFilesInDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        long numFiles = 0;
        try (Stream<Path> files = Files.list(path)) {
            numFiles = files.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return (int) numFiles;
    }

    //Method for creating a directory
    public void createDirectory(String directoryPath, String name) throws IOException {
        Path newDirectory = Paths.get(directoryPath, name);
        Files.createDirectory(newDirectory);
    }

    //Method for getting file names
    public List<String> getFileNames(String directoryPath) {
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

    public String moveFiles(String sourceDirectoryPath, String weather) throws IOException {

        //Get all the files in a directory and adds the names to a list
        List<String> fileNames = getFileNames(sourceDirectoryPath);
        int folderCount = 0;
        //Loop over every file in the file list
        for (String fileName : fileNames) {
            String fileExtension = getFileExtension(fileName);
            //Gets the path for the current file in loop
            Path sourceFilePath = Paths.get(sourceDirectoryPath, fileName);
            //Creates path for the new folder
            Path targetDirectoryPath = Paths.get(sourceDirectoryPath, weather + "_" + fileExtension.substring(1));

            // Create the new directory if it does not exist
            if (!Files.exists(targetDirectoryPath)) {
                Files.createDirectory(targetDirectoryPath);
                folderCount++;
            }

            // Move the file with name change if necessary
            moveFileWithNameChange(sourceFilePath, targetDirectoryPath);
        }
        //Summary of operations
        return ("Number of files moved: " + fileNames.size() + "\nNumber of subdirectories made: " + folderCount);

    }

    //Method for getting file type.
    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }


    //Method for getting all files in subdirectores at a given path and moving them out to the given path and deleting subdirectories
    //Just for ease of testing
    public void resetFiles() throws IOException {

        System.out.println("Enter a path to reset folder: ");
        String userInput = scanner.nextLine();

        Path path = Paths.get(userInput);

        try (Stream<Path> folders = Files.walk(path)) {
            folders.filter(Files::isRegularFile).forEach(file -> {
                try {
                    moveFileWithNameChange(file, path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        deleteEmptyFolders(path);
    }

    //moves a file and name changes it if needed.
    public void moveFileWithNameChange(Path filePath, Path targetLocation) throws IOException {
        String originalName = filePath.getFileName().toString();
        String baseName = originalName;
        String extension = "";
        int dotIndex = originalName.lastIndexOf(".");
        //get name of file and extension
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
            baseName = originalName.substring(0, dotIndex);
        }
        String newName = baseName + extension;
        //get path for file
        Path checkTargetLocation = targetLocation.resolve(newName);
        int counter = 0;
        //if path already taken loop until a number inside () thats not taken
        if (!filePath.getParent().equals(targetLocation)) {
            while (Files.exists(checkTargetLocation)) {
                counter++;
                newName = baseName + "(" + counter + ")" + extension;
                checkTargetLocation = targetLocation.resolve(newName);
            }
            Files.move(filePath, targetLocation.resolve(newName));
        }
    }

    //Method to delete empty folders
    public void deleteEmptyFolders(Path filePath) throws IOException {
        List<Path> directories;

        // get all sudfolders at a path
        try (Stream<Path> directoryPath = Files.walk(filePath).filter(Files::isDirectory)) {
            directories = directoryPath.collect(Collectors.toList());
        }
        //loop over each folder and check if its empty then delete it if empty
        for (Path directory : directories) {
            try {
                if (checkIfEmpty(directory)) {
                    Files.delete(directory);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Method checks if folder is empty
    public boolean checkIfEmpty(Path directory) throws IOException {
        long check;
        try (Stream<Path> files = Files.walk(directory)) {
            check = files.count();
            return check <= 1;
        }
    }
}

