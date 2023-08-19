package com.aayushatharva.cloudaddr.vultr;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsv;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsvGenerator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VultrGenerator {

    private static final int VULTR_ASN = 20473;

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        List<DbIpCsv> dbIpCsvList = DbIpCsvGenerator.generateList();
        List<DbIpCsv> dbIpCsvListVultr = dbIpCsvList.stream()
                .filter(dbIpCsv -> dbIpCsv.asn() == VULTR_ASN)
                .toList();

        List<String> ipv4Prefixes = new ArrayList<>();
        List<String> ipv6Prefixes = new ArrayList<>();
        DbIpCsvGenerator.generatePrefixes(dbIpCsvListVultr, ipv4Prefixes, ipv6Prefixes);

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/vultr/vultr-ipv4.json", new Prefixes("Vultr", ipv4Prefixes));
        FileWriter.writeTextFile("data/vultr/vultr-ipv4.txt", new Prefixes("Vultr", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/vultr/vultr-ipv6.json", new Prefixes("Vultr", ipv6Prefixes));
        FileWriter.writeTextFile("data/vultr/vultr-ipv6.txt", new Prefixes("Vultr", ipv6Prefixes));
    }
}
