package com.aayushatharva.cloudaddr.generator;

import com.aayushatharva.cloudaddr.aws.AwsGenerator;
import com.aayushatharva.cloudaddr.azure.AzureGenerator;
import com.aayushatharva.cloudaddr.cloudflare.CloudflareGenerator;
import com.aayushatharva.cloudaddr.digitalocean.DigialOceanGenerator;
import com.aayushatharva.cloudaddr.gcp.GCPGenerator;
import com.aayushatharva.cloudaddr.hetzner.HetznerGenerator;
import com.aayushatharva.cloudaddr.ibm.IBMGenerator;
import com.aayushatharva.cloudaddr.linode.LinodeGenerator;
import com.aayushatharva.cloudaddr.oracle.OracleGenerator;
import com.aayushatharva.cloudaddr.ovh.OVHGenerator;
import com.aayushatharva.cloudaddr.scaleway.Scaleway;
import com.aayushatharva.cloudaddr.tencent.TencentGenerator;
import com.aayushatharva.cloudaddr.vultr.VultrGenerator;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException {

        // Generate AWS prefixes
        AwsGenerator.main(args);

        // Generate Azure prefixes
        AzureGenerator.main(args);

        // Generate Cloudflare prefixes
        CloudflareGenerator.main(args);

        // DigitalOcean
        DigialOceanGenerator.main(args);

        // GCP
        GCPGenerator.main(args);

        // Hetzner
        HetznerGenerator.main(args);

        // IBM
        IBMGenerator.main(args);

        // Linode
        LinodeGenerator.main(args);

        // Oracle
        OracleGenerator.main(args);

        // OVH
        OVHGenerator.main(args);

        // Scaleway
        Scaleway.main(args);

        // Tencent
        TencentGenerator.main(args);

        // Vultr
        VultrGenerator.main(args);

        // Merge all prefixes
        AllMergedGenerator.main(args);
    }
}
