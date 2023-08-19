package com.aayushatharva.cloudaddr.oracle;

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
import java.util.ArrayList;
import java.util.List;

import static com.aayushatharva.cloudaddr.core.Utils.IPV4_PATTERN_CIDR;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://docs.oracle.com/en-us/iaas/tools/public_ip_ranges.json"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Oracle oracle = mapper.readValue(response.body(), Oracle.class);

        // Retrieve all prefixes
        List<String> oraclePrefixes = oracle.regions()
                .stream()
                .map(Oracle.Region::cidrs)
                .map(cidrs -> cidrs.stream().map(Oracle.Region.CIDR::cidr).toList())
                .flatMap(List::stream)
                .toList();

        List<String> ipv4Prefixes = new ArrayList<>();
        List<String> ipv6Prefixes = new ArrayList<>();

        oraclePrefixes.forEach(prefix -> {
            if (IPV4_PATTERN_CIDR.matcher(prefix).matches()) {
                ipv4Prefixes.add(prefix);
            } else {
                ipv6Prefixes.add(prefix);
            }
        });

        ipv4Prefixes.sort(IPv4AddressComparator.INSTANCE);
        ipv6Prefixes.sort(IPv6AddressComparator.INSTANCE);

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/oci/oci-ipv4.json", new Prefixes("OCI", ipv4Prefixes));
        FileWriter.writeTextFile("data/oci/oci-ipv4.txt", new Prefixes("OCI", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/oci/oci-ipv6.json", new Prefixes("OCI", ipv6Prefixes));
        FileWriter.writeTextFile("data/oci/oci-ipv6.txt", new Prefixes("OCI", ipv6Prefixes));
    }
}
