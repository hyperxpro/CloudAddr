package com.aayushatharva.cloudaddr.gcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPv4AddressComparator;
import com.aayushatharva.cloudaddr.core.IPv6AddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .GET()
                .uri(URI.create("https://www.gstatic.com/ipranges/cloud.json"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        GCP gcp = objectMapper.readValue(httpResponse.body(), GCP.class);

        // Remove duplicates and sort for IPv4
        List<GCP.Prefix> ipv4Prefixes = gcp.prefixes().stream()
                .filter(prefix -> prefix.ipv4Prefix() != null)
                .collect(Collectors.toMap(GCP.Prefix::ipv4Prefix, Function.identity(), (p1, p2) -> p1))
                .values()
                .stream()
                .sorted(Comparator.comparing(GCP.Prefix::ipv4Prefix, IPv4AddressComparator.INSTANCE))
                .toList();

        // Remove duplicates and sort for IPv6
        List<GCP.Prefix> ipv6Prefixes = gcp.prefixes().stream()
                .filter(prefix -> prefix.ipv6Prefix() != null)
                .collect(Collectors.toMap(GCP.Prefix::ipv6Prefix, Function.identity(), (p1, p2) -> p1))
                .values()
                .stream()
                .sorted(Comparator.comparing(GCP.Prefix::ipv6Prefix, IPv6AddressComparator.INSTANCE))
                .toList();

        // Map prefix to string
        List<String> ipv4 = ipv4Prefixes.stream()
                .map(GCP.Prefix::ipv4Prefix)
                .toList();

        // Map prefix to string
        List<String> ipv6 = ipv6Prefixes.stream()
                .map(GCP.Prefix::ipv6Prefix)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/gcp/gcp-ipv4.json", new Prefixes("GCP", ipv4));
        FileWriter.writeTextFile("data/gcp/gcp-ipv4.txt", new Prefixes("GCP", ipv4));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/gcp/gcp-ipv6.json", new Prefixes("GCP", ipv6));
        FileWriter.writeTextFile("data/gcp/gcp-ipv6.txt", new Prefixes("GCP", ipv6));
    }
}
