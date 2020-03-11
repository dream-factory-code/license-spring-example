package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.Product;
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
        inputStream.close();

        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey(obj.get("apiKey"))
                .productCode(obj.get("productCode"))
                .sharedKey(obj.get("sharedKey"))
                .appName(obj.get("appName"))
                .appVersion(obj.get("appVersion"))
                //redundant
                .serviceURL(obj.get("serviceURL"))
                .build();

        System.out.println(configuration);
        LicenseManager manager = LicenseManager.getInstance();

        manager.initialize(configuration);

        Product product = manager.getProductDetails();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        if (product.getAuthorizationMethod().equals("user")){
            System.out.println("Please enter username:");
            String username = reader.readLine();

            System.out.println("Please enter password:");
            String password = reader.readLine();
            activateUser(username, password);
        } else {
            System.out.println("Please enter key:");
            String key = reader.readLine();
            activate(key);
        }
        System.out.println("License successfully activated.");
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
