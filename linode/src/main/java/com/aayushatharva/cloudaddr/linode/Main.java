package com.aayushatharva.cloudaddr.linode;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPv4AddressComparator;
import com.aayushatharva.cloudaddr.core.IPv6AddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.aayushatharva.cloudaddr.core.Utils.IPV4_PATTERN_CIDR;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://geoip.linode.com/"))
                .GET()
                .build();

        HttpResponse<Stream<String>> response = httpClient.send(request, HttpResponse.BodyHandlers.ofLines());
        List<String> prefixes = response.body()
                .filter(s -> !s.startsWith("#"))
                .map(s -> s.split(",")[0])
                .toList();

        List<String> digitalOceanIpv4Addresses = new ArrayList<>();
        List<String> digitalOceanIpv6Addresses = new ArrayList<>();

        // Collect all IPv4 and IPv6 addresses
        prefixes.forEach(prefix -> {
            if (IPV4_PATTERN_CIDR.matcher(prefix).matches()) {
                digitalOceanIpv4Addresses.add(prefix);
            } else {
                digitalOceanIpv6Addresses.add(prefix);
            }
        });

        // Sort and remove duplicates
        List<String> ipv4Prefixes = digitalOceanIpv4Addresses.stream()
                .distinct()
                .sorted(IPv4AddressComparator.INSTANCE)
                .toList();

        // Sort and remove duplicates
        List<String> ipv6Prefixes = digitalOceanIpv6Addresses.stream()
                .distinct()
                .sorted(IPv6AddressComparator.INSTANCE)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/linode/linode-ipv4.json", new Prefixes("Linode", ipv4Prefixes));
        FileWriter.writeTextFile("data/linode/linode-ipv4.txt", new Prefixes("Linode", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/linode/linode-ipv6.json", new Prefixes("Linode", ipv6Prefixes));
        FileWriter.writeTextFile("data/linode/linode-ipv6.txt", new Prefixes("Linode", ipv6Prefixes));
    }
}
