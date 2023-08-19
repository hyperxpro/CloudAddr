package com.aayushatharva.cloudaddr.dbipcsv;

/**
 * Represents a row in CSV file
 *
 * @param start  Start IP address
 * @param end    End IP address
 * @param asn    Autonomous System Number
 * @param asnOrg Autonomous System Organization
 */
public record DbIpCsv(String start, String end, int asn, String asnOrg) {

    public DbIpCsv(String[] line) {
        this(line[0], line[1], Integer.parseInt(line[2]), line[3]);
    }
}
