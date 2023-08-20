package com.aayushatharva.cloudaddr.worldstream;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsv;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsvGenerator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorlstreamGenerator {

    private static final int WORLDSTREAM_ASN = 49981;

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        List<DbIpCsv> dbIpCsvList = DbIpCsvGenerator.generateList();
        List<DbIpCsv> dbIpCsvListVultr = dbIpCsvList.stream()
                .filter(dbIpCsv -> dbIpCsv.asn() == WORLDSTREAM_ASN)
                .toList();

        List<String> ipv4Prefixes = new ArrayList<>();
        List<String> ipv6Prefixes = new ArrayList<>();
        DbIpCsvGenerator.generatePrefixes(dbIpCsvListVultr, ipv4Prefixes, ipv6Prefixes);

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/worldstream/worldstream-ipv4.json", new Prefixes("Worldstream", ipv4Prefixes));
        FileWriter.writeTextFile("data/worldstream/worldstream-ipv4.txt", new Prefixes("Worldstream", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/worldstream/worldstream-ipv6.json", new Prefixes("Worldstream", ipv6Prefixes));
        FileWriter.writeTextFile("data/worldstream/worldstream-ipv6.txt", new Prefixes("Worldstream", ipv6Prefixes));
    }
}
