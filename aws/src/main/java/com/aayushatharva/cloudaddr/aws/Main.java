package com.aayushatharva.cloudaddr.aws;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPv4AddressComparator;
import com.aayushatharva.cloudaddr.core.IPv6AddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .GET()
                .uri(URI.create("https://ip-ranges.amazonaws.com/ip-ranges.json"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        AWS aws = objectMapper.readValue(httpResponse.body(), AWS.class);

        // Remove duplicates and sort
        List<AWS.PrefixIpv4> awsIpv4Prefixes = aws.prefixes().stream()
                .collect(Collectors.toMap(AWS.PrefixIpv4::ip_prefix, prefixIpv4 -> prefixIpv4, (p1, p2) -> p1))
                .values()
                .stream()
                .sorted(Comparator.comparing(AWS.PrefixIpv4::ip_prefix, IPv4AddressComparator.INSTANCE))
                .toList();

        // Remove duplicates and sort
        List<AWS.PrefixIpv6> awsIpv6Prefixes = aws.ipv6_prefixes().stream()
                .collect(Collectors.toMap(AWS.PrefixIpv6::ipv6_prefix, prefixIpv6 -> prefixIpv6, (p1, p2) -> p1))
                .values()
                .stream()
                .sorted(Comparator.comparing(AWS.PrefixIpv6::ipv6_prefix, IPv6AddressComparator.INSTANCE))
                .toList();

        // Map prefix to string
        List<String> ipv4 = awsIpv4Prefixes.stream()
                .map(AWS.PrefixIpv4::ip_prefix)
                .toList();

        // Map prefix to string
        List<String> ipv6 = awsIpv6Prefixes.stream()
                .map(AWS.PrefixIpv6::ipv6_prefix)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/aws/aws-ipv4.json", new Prefixes("AWS", ipv4));
        FileWriter.writeTextFile("data/aws/aws-ipv4.txt", new Prefixes("AWS", ipv4));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/aws/aws-ipv6.json", new Prefixes("AWS", ipv6));
        FileWriter.writeTextFile("data/aws/aws-ipv6.txt", new Prefixes("AWS", ipv6));
    }
}
