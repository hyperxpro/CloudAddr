package com.aayushatharva.cloudaddr.dbipcsv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DbIpCsvReader {

    /**
     * Read CSV file and return {@link List} of {@link DbIpCsv} objects
     *
     * @param filePath Path to CSV file
     * @return {@link List} of {@link DbIpCsv} objects
     * @throws IOException            If an I/O error occurs
     * @throws CsvValidationException If an error occurs while validating CSV file
     */
    public static List<DbIpCsv> read(String filePath) throws IOException, CsvValidationException {
        return read(new ArrayList<>(), filePath);
    }

    /**
     * Read CSV file and return {@link List} of {@link DbIpCsv} objects
     *
     * @param dbIpList {@link List} where {@link DbIpCsv} objects will be added
     * @param filePath Path to CSV file
     * @return {@link List} of {@link DbIpCsv} objects
     * @throws IOException            If an I/O error occurs
     * @throws CsvValidationException If an error occurs while validating CSV file
     */
    public static List<DbIpCsv> read(List<DbIpCsv> dbIpList, String filePath) throws IOException, CsvValidationException {
        return read(dbIpList, new BufferedReader(new FileReader(filePath)));
    }

    /**
     * Read CSV file from Tar.gz archive and return {@link List} of {@link DbIpCsv} objects
     *
     * @param dbIpList    {@link List} where {@link DbIpCsv} objects will be added
     * @param inputStream {@link InputStream} of Tar.gz archive
     * @return {@link List} of {@link DbIpCsv} objects
     * @throws IOException            If an I/O error occurs
     * @throws CsvValidationException If an error occurs while validating CSV file
     */
    public static List<DbIpCsv> decompressGzAndRead(List<DbIpCsv> dbIpList, InputStream inputStream) throws IOException, CsvValidationException {
        return read(dbIpList, decompressAndReturnReader(inputStream));
    }

    /**
     * Read CSV file from {@link Reader} and return {@link List} of {@link DbIpCsv} objects
     *
     * @param dbIpList  {@link List} where {@link DbIpCsv} objects will be added
     * @param csvReader {@link Reader} for CSV file
     * @return {@link List} of {@link DbIpCsv} objects
     * @throws IOException            If an I/O error occurs
     * @throws CsvValidationException If an error occurs while validating CSV file
     */
    public static List<DbIpCsv> read(List<DbIpCsv> dbIpList, Reader csvReader) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(csvReader)) {
            do {
                dbIpList.add(new DbIpCsv(reader.readNext()));
            } while (reader.peek() != null);
            return dbIpList;
        }
    }

    private static InputStreamReader decompressAndReturnReader(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(inputStream)) {
            int count;
            byte[] data = new byte[16384];

            while ((count = gzipIn.read(data, 0, 16384)) != -1) {
                outputStream.write(data, 0, count);
            }

            outputStream.flush();
            gzipIn.close();

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                return new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8);
            }
        }
    }

    /**
     * Get current month and year in UTC like: 10-2023
     *
     * @return {@link String} array of month and year; month is at index 0 and year is at index 1
     */
    public static String[] currentMonthAndYear() {
        String MONTH;
        String YEAR;

        ZoneId z = ZoneId.of("UTC");
        ZonedDateTime zdt = ZonedDateTime.now(z);
        String month = String.valueOf(zdt.getMonthValue());
        if (month.length() == 1) {
            MONTH = "0" + month;
        } else {
            MONTH = month;
        }
        YEAR = String.valueOf(zdt.getYear());
        return new String[]{MONTH, YEAR};
    }
}
