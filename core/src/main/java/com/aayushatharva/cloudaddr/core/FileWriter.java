package com.aayushatharva.cloudaddr.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

public final class FileWriter {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Write the {@link Prefixes} to the file
     *
     * @param fileName The file name
     * @param prefixes {@link Prefixes} instance
     * @throws IOException If an I/O error occurs
     */
    public static void writeJsonFile(String fileName, Prefixes prefixes) throws IOException {
        createMissingFolders(fileName);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(fileName)) {
            fileWriter.write(GSON.toJson(prefixes));
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Wrote File: " + fileName);
        System.out.println("Provider: " + prefixes.cloudProvider());
        System.out.println("Prefixes: " + prefixes.prefixes().size());
        System.out.println("-----------------------------------------------");
    }

    /**
     * Write {@link Prefixes} to the text file
     *
     * @param fileName The file name
     * @param prefixes {@link Prefixes} instance
     * @throws IOException If an I/O error occurs
     */
    public static void writeTextFile(String fileName, Prefixes prefixes) throws IOException {
        createMissingFolders(fileName);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(fileName)) {
            for (String prefix : prefixes.prefixes()) {
                fileWriter.write(prefix);
                fileWriter.write(System.lineSeparator());
            }
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Wrote File: " + fileName);
        System.out.println("Provider: " + prefixes.cloudProvider());
        System.out.println("Prefixes: " + prefixes.prefixes().size());
        System.out.println("-----------------------------------------------");
    }

    private static void createMissingFolders(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Failed to create folders.");
            }
        }
    }
}
