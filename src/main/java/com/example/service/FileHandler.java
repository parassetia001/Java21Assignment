package com.example.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileHandler {
    public static void saveToFile(String fileName, List<String> data) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public static List<String> readFromFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllLines(path);
    }
}
