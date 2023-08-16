package io.github.secwrk.cloudaddr.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(fileName)) {
            fileWriter.write(GSON.toJson(prefixes));
        }
    }

    /**
     * Write {@link Prefixes} to the text file
     *
     * @param fileName The file name
     * @param prefixes {@link Prefixes} instance
     * @throws IOException If an I/O error occurs
     */
    public static void writeTextFile(String fileName, Prefixes prefixes) throws IOException {
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(fileName)) {
            for (String prefix : prefixes.prefixes()) {
                fileWriter.write(prefix);
                fileWriter.write(System.lineSeparator());
            }
        }
    }
}
