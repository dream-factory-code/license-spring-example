package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.model.ActivationLicense;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(Paths.get("config.yaml"));
        Map<String, String> obj = yaml.load(inputStream);

        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey(obj.get("apiKey"))
                .productCode(obj.get("productCode"))
                .sharedKey(obj.get("sharedKey"))
                .appName(obj.get("appName"))
                .appVersion(obj.get("appVersion"))
                //redundant
                .serviceURL(obj.get("serviceURL"))
                .build();

        inputStream.close();

        System.out.println(configuration);
        LicenseManager manager = LicenseManager.getInstance();

        manager.initialize(configuration);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Would you like to enter a license using your username and password (press \"u\")\n" +
                "or with a license key (press \"k\").");
        String choice = reader.readLine();

        if (choice.equals("u")){
            System.out.println("\nPlease enter username:");
            String username = reader.readLine();

            System.out.println("Please enter password:");
            String password = reader.readLine();
            activateUser(username, password);
        } else {
            System.out.println("\nPlease enter key:");
            String key = reader.readLine();
            activate(key);
        }
        System.out.println("License successfully issued.");
}

    private static void activateUser(String username, String password) {

        LicenseManager instance = LicenseManager.getInstance();

        License license = instance.activateLicense(ActivationLicense.fromUsername(username, password));

    }

    private static void activate(String key) {

        LicenseManager instance = LicenseManager.getInstance();

        License license = instance.activateLicense(ActivationLicense.fromKey(key));

    }

}
