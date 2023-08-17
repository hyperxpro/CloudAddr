package io.github.secwrk.cloudaddr.azure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.secwrk.cloudaddr.core.FileWriter;
import io.github.secwrk.cloudaddr.core.IPv4AddressComparator;
import io.github.secwrk.cloudaddr.core.IPv6AddressComparator;
import io.github.secwrk.cloudaddr.core.Prefixes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public final class Main {

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(3[0-2]|[1-2]?[0-9])$");

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> dates = last7Days();

        Azure azure = null;

        for (String date : dates) {
            HttpResponse<String> response = response(date);
            if (response != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                azure = objectMapper.readValue(response.body(), Azure.class);
                break;
            }
        }

        if (azure == null) {
            System.out.println("No response from Microsoft for the last 7 days");
            return;
        }

        List<String> azureIpv4Addresses = new ArrayList<>();
        List<String> azureIpv6Addresses = new ArrayList<>();

        // Collect all IPv4 and IPv6 addresses
        azure.values().forEach(value -> {
            if (value.properties().addressPrefixes() != null) {
                value.properties().addressPrefixes().forEach(addressPrefix -> {
                    if (IPV4_PATTERN.matcher(addressPrefix).matches()) {
                        azureIpv4Addresses.add(addressPrefix);
                    } else {
                        azureIpv6Addresses.add(addressPrefix);
                    }
                });
            }
        });

        // Sort and remove duplicates
        List<String> ipv4Prefixes = azureIpv4Addresses.stream()
                .distinct()
                .sorted(IPv4AddressComparator.INSTANCE)
                .toList();

        // Sort and remove duplicates
        List<String> ipv6Prefixes = azureIpv6Addresses.stream()
                .distinct()
                .sorted(IPv6AddressComparator.INSTANCE)
                .toList();

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/azure/azure-ipv4.json", new Prefixes("Azure", ipv4Prefixes));
        FileWriter.writeTextFile("data/azure/azure-ipv4.txt", new Prefixes("Azure", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/azure/azure-ipv6.json", new Prefixes("Azure", ipv6Prefixes));
        FileWriter.writeTextFile("data/azure/azure-ipv6.txt", new Prefixes("Azure", ipv6Prefixes));
    }

    private static HttpResponse<String> response(String date) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://download.microsoft.com/download/7/1/D/71D86715-5596-4529-9B13-DA13A5DE5B63/ServiceTags_Public_" + date + ".json"))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.out.println("Response code: " + response.statusCode() + " for date: " + date);
            return null;
        } else {
            return response;
        }
    }

    private static List<String> last7Days() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();

        List<String> dates = new ArrayList<>();

        // Print the current date
        Date currentDate = calendar.getTime();
        String currentDateString = dateFormat.format(currentDate);
        dates.add(currentDateString);

        // Print the dates of the last 7 days
        for (int i = 1; i <= 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, -1); // Subtract one day
            Date previousDate = calendar.getTime();
            String previousDateString = dateFormat.format(previousDate);
            dates.add(previousDateString);
        }
        return dates;
    }
}
