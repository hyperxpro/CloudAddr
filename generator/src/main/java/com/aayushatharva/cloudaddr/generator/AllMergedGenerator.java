package com.aayushatharva.cloudaddr.generator;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.IPAddressComparator;
import com.aayushatharva.cloudaddr.core.Prefixes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AllMergedGenerator {

    public static void main(String[] args) throws IOException {
        String rootFolderPath = "data/";

        List<String> ipv4Files = new ArrayList<>();
        searchForFiles(ipv4Files, rootFolderPath, true);

        List<String> ipv6Files = new ArrayList<>();
        searchForFiles(ipv6Files, rootFolderPath, false);

        List<String> ipv4Prefixes = ipv4Files.stream()
                .map(path -> {
                    try {
                        return Files.readAllLines(Paths.get(path));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(List::stream)
                .sorted(IPAddressComparator.INSTANCE)
                .toList();

        List<String> ipv6Prefixes = ipv6Files.stream()
                .map(path -> {
                    try {
                        return Files.readAllLines(Paths.get(path));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(List::stream)
                .sorted(IPAddressComparator.INSTANCE)
                .toList();

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/all/all-ipv4.json", new Prefixes("ALL", ipv4Prefixes));
        FileWriter.writeTextFile("data/all/all-ipv4.txt", new Prefixes("ALL", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/all/all-ipv6.json", new Prefixes("ALL", ipv6Prefixes));
        FileWriter.writeTextFile("data/all/all-ipv6.txt", new Prefixes("ALL", ipv6Prefixes));
    }

    public static void searchForFiles(List<String> filesList, String folderPath, boolean ipv4) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        String searchFileName = ipv4 ? "ipv4.txt" : "ipv6.txt";
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchForFiles(filesList, file.getAbsolutePath(), ipv4); // Recurse into subfolder
                } else if (file.getName().endsWith(searchFileName) && !file.getName().contains("all-")) {
                    filesList.add(file.getAbsolutePath());
                }
            }
        }
    }
}
