package com.aayushatharva.cloudaddr.scaleway;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsv;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsvGenerator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scaleway {

    private static final int SCALEWAY_ASN = 12876;

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        List<DbIpCsv> dbIpCsvList = DbIpCsvGenerator.generateList();
        List<DbIpCsv> dbIpCsvListForAsn = dbIpCsvList.stream()
                .filter(dbIpCsv -> dbIpCsv.asn() == SCALEWAY_ASN)
                .toList();

        List<String> ipv4Prefixes = new ArrayList<>();
        List<String> ipv6Prefixes = new ArrayList<>();
        DbIpCsvGenerator.generatePrefixes(dbIpCsvListForAsn, ipv4Prefixes, ipv6Prefixes);

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/scaleway/scaleway-ipv4.json", new Prefixes("Scaleway", ipv4Prefixes));
        FileWriter.writeTextFile("data/scaleway/scaleway-ipv4.txt", new Prefixes("Scaleway", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/scaleway/scaleway-ipv6.json", new Prefixes("Scaleway", ipv6Prefixes));
        FileWriter.writeTextFile("data/scaleway/scaleway-ipv6.txt", new Prefixes("Scaleway", ipv6Prefixes));
    }
}
