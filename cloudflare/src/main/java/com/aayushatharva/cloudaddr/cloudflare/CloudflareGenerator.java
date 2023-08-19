package com.aayushatharva.cloudaddr.cloudflare;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.Prefixes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;

public class CloudflareGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest requestIpv4 = HttpRequest.newBuilder()
                .uri(URI.create("https://www.cloudflare.com/ips-v4"))
                .GET()
                .build();

        HttpRequest requestIpv6 = HttpRequest.newBuilder()
                .uri(URI.create("https://www.cloudflare.com/ips-v6"))
                .GET()
                .build();

        HttpResponse<Stream<String>> responseIpv4 = httpClient.send(requestIpv4, HttpResponse.BodyHandlers.ofLines());
        HttpResponse<Stream<String>> responseIpv6 = httpClient.send(requestIpv6, HttpResponse.BodyHandlers.ofLines());

        List<String> ipv4 = responseIpv4.body().toList();
        List<String> ipv6 = responseIpv6.body().toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/cloudflare/cloudflare-ipv4.json", new Prefixes("Cloudflare", ipv4));
        FileWriter.writeTextFile("data/cloudflare/cloudflare-ipv4.txt", new Prefixes("Cloudflare", ipv4));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/cloudflare/cloudflare-ipv6.json", new Prefixes("Cloudflare", ipv6));
        FileWriter.writeTextFile("data/cloudflare/cloudflare-ipv6.txt", new Prefixes("Cloudflare", ipv6));
    }
}
