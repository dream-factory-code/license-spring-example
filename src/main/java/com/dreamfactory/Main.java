package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.Product;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        Yaml yaml = new Yaml();
        Path path = Paths.get("config.yaml");
        Configuration configuration = null;

        try (InputStream inputStream = Files.newInputStream(path)){
            configuration = yaml.loadAs(inputStream, Configuration.class);
        }

        LicenseManager manager = LicenseManager.getInstance();
        manager.initialize(configuration.toLicenseSpringConfiguration());

        Product product = manager.getProductDetails();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            if (product.getAuthorizationMethod().equals("user")){
                System.out.println("\nPlease enter username:");
                String username = reader.readLine();

                System.out.println("\nPlease enter password:");
                String password = reader.readLine();
                activateUser(username, password);
            } else {
                System.out.println("\nPlease enter key:");
                String key = reader.readLine();
                activate(key);
            }
            System.out.println("\nLicense successfully activated.");
        }


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
