package com.aayushatharva.cloudaddr.tencent;

import com.aayushatharva.cloudaddr.core.FileWriter;
import com.aayushatharva.cloudaddr.core.Prefixes;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsv;
import com.aayushatharva.cloudaddr.dbipcsv.DbIpCsvGenerator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TencentGenerator {

    private static final int TENCENT_ASN = 132203;

    public static void main(String[] args) throws CsvValidationException, IOException, InterruptedException {
        List<DbIpCsv> dbIpCsvList = DbIpCsvGenerator.generateList();
        List<DbIpCsv> dbIpCsvListForAsn = dbIpCsvList.stream()
                .filter(dbIpCsv -> dbIpCsv.asn() == TENCENT_ASN)
                .toList();

        List<String> ipv4Prefixes = new ArrayList<>();
        List<String> ipv6Prefixes = new ArrayList<>();
        DbIpCsvGenerator.generatePrefixes(dbIpCsvListForAsn, ipv4Prefixes, ipv6Prefixes);

        // Write IPv4 prefixes to file
        FileWriter.writeJsonFile("data/tencent/tencent-ipv4.json", new Prefixes("Tencent", ipv4Prefixes));
        FileWriter.writeTextFile("data/tencent/tencent-ipv4.txt", new Prefixes("Tencent", ipv4Prefixes));

        // Write IPv6 prefixes to file
        FileWriter.writeJsonFile("data/tencent/tencent-ipv6.json", new Prefixes("Tencent", ipv6Prefixes));
        FileWriter.writeTextFile("data/tencent/tencent-ipv6.txt", new Prefixes("Tencent", ipv6Prefixes));
    }
}
