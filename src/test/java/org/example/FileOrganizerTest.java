package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileOrganizerTest {

    private Path tempDirectory;

    @BeforeEach
    public void setUpFiles() throws IOException {
        tempDirectory = Files.createTempDirectory("test");
    }

    @AfterEach
    public void cleanUpFiles() throws IOException {
        try (Stream<Path> folders = Files.walk(tempDirectory)) {
            folders
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
    }

    @Test
    void countFilesInDirectoryTest() throws IOException {
        Files.createFile(tempDirectory.resolve("file1.txt"));
        Files.createFile(tempDirectory.resolve("file2.txt"));
        Files.createFile(tempDirectory.resolve("file3.docx"));

        FileOrganizer fileOrganizer = new FileOrganizer();

        int numFiles = fileOrganizer.countFilesInDirectory(tempDirectory.toString());

        assertEquals(3, numFiles);
    }

    @Test
    void createDirectoryTest() throws IOException {

        FileOrganizer fileOrganizer = new FileOrganizer();

        fileOrganizer.createDirectory(tempDirectory.toString(), "test");
        fileOrganizer.createDirectory(tempDirectory.toString(), "test2");
        fileOrganizer.createDirectory(tempDirectory.toString(), "test3");
        fileOrganizer.createDirectory(tempDirectory.toString(), "test4");

        try (Stream<Path> folders = Files.walk(tempDirectory)) {
            List<Path> directories = folders.collect(Collectors.toList());
            //Have to -1 on directories because its includes the root directory in Files.walk
            assertEquals(4, directories.size() - 1);
        }
    }

    @Test
    void getFileNamesTest() throws IOException {
        FileOrganizer fileOrganizer = new FileOrganizer();

        Files.createFile(tempDirectory.resolve("test.txt"));
        Files.createFile(tempDirectory.resolve("test1.docx"));
        Files.createFile(tempDirectory.resolve("test2.pdf"));

        List<String> filesNames = fileOrganizer.getFileNames(tempDirectory.toString());

        assertEquals(3, filesNames.size());
        assertEquals("test.txt", filesNames.get(0));
        assertEquals("test1.docx", filesNames.get(1));
        assertEquals("test2.pdf", filesNames.get(2));
    }

    @Test
    void moveFilesTest() throws IOException {

        String weather = "cloudy";
        FileOrganizer fileOrganizer = new FileOrganizer();

        Files.createFile(tempDirectory.resolve("test.txt"));
        Files.createFile(tempDirectory.resolve("test1.docx"));
        Files.createFile(tempDirectory.resolve("test2.pdf"));

        try (Stream<Path> folders = Files.walk(tempDirectory)) {

            List<Path> directories = folders.collect(Collectors.toList());
            String result = fileOrganizer.moveFiles(tempDirectory.toString(), weather);

            //Checks number of directories made is correct
            assertEquals(3, directories.size() - 1);

            //checks that name of the directories is right
            assertTrue(Files.exists(tempDirectory.resolve(weather + "_txt")));
            assertTrue(Files.exists(tempDirectory.resolve(weather + "_docx")));
            assertTrue(Files.exists(tempDirectory.resolve(weather + "_pdf")));
            //false check
            assertFalse(Files.exists(tempDirectory.resolve(weather + "_rtf")));

            //Checks that the returned string from moveFiles is correct
            assertEquals("Number of files moved: 3\nNumber of subdirectories made: 3", result);
        }

    }

    @Test
    void getFileExtention() {
        String txt = "test.txt";
        String pdf = "test2.pdf";
        String test = "test3.3232.asdasd.ap";
        FileOrganizer fileOrganizer = new FileOrganizer();
        assertEquals(".txt", fileOrganizer.getFileExtension(txt));
        assertEquals(".pdf", fileOrganizer.getFileExtension(pdf));
        assertEquals(".ap", fileOrganizer.getFileExtension(test));
    }
}
