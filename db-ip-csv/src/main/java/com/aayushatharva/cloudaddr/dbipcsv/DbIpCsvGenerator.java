package com.aayushatharva.cloudaddr.dbipcsv;

import com.aayushatharva.cloudaddr.core.IPAddressComparator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.aayushatharva.cloudaddr.core.Utils.IPV4_PATTERN;
import static com.aayushatharva.cloudaddr.core.Utils.generateCidr;

public class DbIpCsvGenerator {

    /**
     * Generates a list of {@link DbIpCsv} objects by downloading it from <a href="https://db-ip.com/db/download/ip-to-asn-lite">db-ip.com</a>
     *
     * @return List of {@link DbIpCsv} objects (unmodifiable)
     * @throws IOException            If an I/O error occurs
     * @throws CsvValidationException If an error occurs while validating CSV file
     * @throws InterruptedException   If the operation is interrupted
     */
    public static List<DbIpCsv> generateList() throws CsvValidationException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String[] currentMonthAndYear = DbIpCsvReader.currentMonthAndYear();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://download.db-ip.com/free/dbip-asn-lite-" + currentMonthAndYear[1] + "-" + currentMonthAndYear[0] + ".csv.gz"))
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return Collections.unmodifiableList(DbIpCsvReader.decompressGzAndRead(new ArrayList<>(), response.body()));
    }

    /**
     * Generates a list of IPv4 and IPv6 prefixes from {@link DbIpCsv} objects
     *
     * @param dbIpCsvList  List of {@link DbIpCsv} objects
     * @param ipv4Prefixes List of IPv4 prefixes
     * @param ipv6Prefixes List of IPv6 prefixes
     */
    public static void generatePrefixes(List<DbIpCsv> dbIpCsvList, List<String> ipv4Prefixes, List<String> ipv6Prefixes) {
        List<String> ipv4PrefixesFiltered = dbIpCsvList.stream()
                .filter(dbIpCsv -> IPV4_PATTERN.matcher(dbIpCsv.start()).matches())
                .map(dbIpCsv -> generateCidr(dbIpCsv.start(), dbIpCsv.end()))
                .flatMap(Arrays::stream)
                .sorted(IPAddressComparator.INSTANCE)
                .toList();

        List<String> ipv6PrefixesFiltered = dbIpCsvList.stream()
                .filter(dbIpCsv -> !IPV4_PATTERN.matcher(dbIpCsv.start()).matches())
                .map(dbIpCsv -> generateCidr(dbIpCsv.start(), dbIpCsv.end()))
                .flatMap(Arrays::stream)
                .sorted(IPAddressComparator.INSTANCE)
                .toList();

        ipv4Prefixes.addAll(ipv4PrefixesFiltered);
        ipv6Prefixes.addAll(ipv6PrefixesFiltered);
    }
}
