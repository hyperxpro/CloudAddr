package com.aayushatharva.cloudaddr;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPv4AddressComparator;
import com.aayushatharva.cloudaddr.core.IPv6AddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsv;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsvReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.aayushatharva.cloudaddr.core.Utils.IPV4_PATTERN;
import static com.aayushatharva.cloudaddr.core.Utils.generateCidr;

public class Main {

    private static final int OVH_ASN = 16276;

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        String[] currentMonthAndYear = DbIpCsvReader.currentMonthAndYear();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://download.db-ip.com/free/dbip-asn-lite-" + currentMonthAndYear[1] + "-" + currentMonthAndYear[0] + ".csv.gz"))
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        List<DbIpCsv> dbIpCsvList = DbIpCsvReader.decompressGzAndRead(new ArrayList<>(), response.body())
                .stream()
                .filter(dbIpCsv -> dbIpCsv.asn() == OVH_ASN)
                .toList();

        List<String> ipv4Prefixes = dbIpCsvList.stream()
                .filter(dbIpCsv -> IPV4_PATTERN.matcher(dbIpCsv.start()).matches())
                .map(dbIpCsv -> generateCidr(dbIpCsv.start(), dbIpCsv.end()))
                .flatMap(Arrays::stream)
                .sorted(IPv4AddressComparator.INSTANCE)
                .toList();

        List<String> ipv6Prefixes = dbIpCsvList.stream()
                .filter(dbIpCsv -> !IPV4_PATTERN.matcher(dbIpCsv.start()).matches())
                .map(dbIpCsv -> generateCidr(dbIpCsv.start(), dbIpCsv.end()))
                .flatMap(Arrays::stream)
                .sorted(IPv6AddressComparator.INSTANCE)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/ovh/ovh-ipv4.json", new Prefixes("OVH", ipv4Prefixes));
        FileWriter.writeTextFile("data/ovh/ovh-ipv4.txt", new Prefixes("OVH", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/ovh/ovh-ipv6.json", new Prefixes("OVH", ipv6Prefixes));
        FileWriter.writeTextFile("data/ovh/ovh-ipv6.txt", new Prefixes("OVH", ipv6Prefixes));
    }
}
