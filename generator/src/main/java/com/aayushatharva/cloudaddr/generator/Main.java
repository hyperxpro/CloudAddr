package com.aayushatharva.cloudaddr.generator;

import com.aayushatharva.cloudaddr.aws.AwsGenerator;
import com.aayushatharva.cloudaddr.azure.AzureGenerator;
import com.aayushatharva.cloudaddr.cloudflare.CloudflareGenerator;
import com.aayushatharva.cloudaddr.datapacket.DatapacketGenerator;
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
import com.aayushatharva.cloudaddr.worldstream.WorlstreamGenerator;
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

        // Generate Datapacket prefixes
        DatapacketGenerator.main(args);

        // DigitalOcean prefixes
        DigialOceanGenerator.main(args);

        // GCP prefixes
        GCPGenerator.main(args);

        // Hetzner prefixes
        HetznerGenerator.main(args);

        // IBM prefixes
        IBMGenerator.main(args);

        // Linode prefixes
        LinodeGenerator.main(args);

        // Oracle prefixes
        OracleGenerator.main(args);

        // OVH prefixes
        OVHGenerator.main(args);

        // Scaleway prefixes
        Scaleway.main(args);

        // Tencent prefixes
        TencentGenerator.main(args);

        // Vultr prefixes
        VultrGenerator.main(args);

        // Worldstream prefixes
        WorlstreamGenerator.main(args);

        // Merge all prefixes
        AllMergedGenerator.main(args);
    }
}
