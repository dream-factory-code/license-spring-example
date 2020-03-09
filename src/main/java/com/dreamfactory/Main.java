package com.dreamfactory;

import com.google.common.io.ByteStreams;
import com.licensespring.LicenseSpringConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(Paths.get("config.yaml"));
        Map<String, String> obj = yaml.load(inputStream);
        LicenseSpringConfiguration licenseSpringConfiguration = LicenseSpringConfiguration.builder()
                .apiKey(obj.get("apiKey"))
                .productCode(obj.get("productCode"))
                .sharedKey(obj.get("sharedKey"))
                .appName(obj.get("appName"))
                .appVersion(obj.get("appVersion"))
                //redundant
                .serviceURL(obj.get("serviceURL"))
                .build();

        System.out.println(licenseSpringConfiguration);

    }








}
