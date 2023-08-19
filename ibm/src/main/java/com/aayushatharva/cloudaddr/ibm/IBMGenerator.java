package com.aayushatharva.cloudaddr.ibm;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPAddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aayushatharva.cloudaddr.core.Utils.isRFC1918;

public class IBMGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://raw.githubusercontent.com/dprosper/cidr-calculator/main/data/datacenters.json"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        List<String> ipv4Prefixes = extractIPAddresses(httpResponse.body()).stream()
                .filter(ipAddress -> {
                    String[] split = ipAddress.split("/");
                    return !isRFC1918(split[0]);
                })
                .sorted(IPAddressComparator.INSTANCE)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/ibm/ibm-ipv4.json", new Prefixes("IBM", ipv4Prefixes));
        FileWriter.writeTextFile("data/ibm/ibm-ipv4.txt", new Prefixes("IBM", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/ibm/ibm-ipv6.json", new Prefixes("IBM", Collections.emptyList()));
        FileWriter.writeTextFile("data/ibm/ibm-ipv6.txt", new Prefixes("IBM", Collections.emptyList()));
    }

    private static List<String> extractIPAddresses(String content) {
        List<String> ipAddresses = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}(?:/\\d{1,2})?\\b"); // Updated regex for IP addresses and CIDR notation

        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String ipAddress = matcher.group();
            ipAddresses.add(ipAddress);
        }

        return ipAddresses;
    }
}
