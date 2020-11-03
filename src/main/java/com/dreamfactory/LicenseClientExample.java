package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.dto.UnactivatedTrialLicense;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.Customer;
import com.licensespring.model.Product;
import com.licensespring.model.exceptions.LicenseSpringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


// An example of how your app code would look like if your product allows trials.
// If your product doesn't allow trial, skip providing a trial license.
public class LicenseClientExample {
    private static final Logger log = LoggerFactory.getLogger(LicenseClientExample.class);
    private static LicenseManager manager;
    private static Product product;

    public static void main(String[] args) throws IOException {
        // Initialization, every time app starts.
        String path = "sample.yaml";
        Yaml yaml = new Yaml(new Constructor(ConfigurationData.class));
        InputStream inputStream = LicenseClientExample.class.getClassLoader()
                .getResourceAsStream(path);

        ConfigurationData data = yaml.load(inputStream);

        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey(data.getApiKey())
                .productCode(data.getProductCode())
                .sharedKey(data.getSharedKey())
                .appName(data.getAppName())
                .appVersion(data.getAppVer())
                .build();

        manager = LicenseManager.getInstance();
        manager.initialize(configuration);

        product = manager.getProductDetails();

        log.info(product.getProductName());
        log.info(product.getShortCode());
        log.info(String.valueOf(product.isAllowTrial()));
        log.info(String.valueOf(product.getTrialDays()));
        log.info(product.getAuthorizationMethod());

        // When user first signs up, provide a trial license, this should be done only once.
        Customer customer = Customer.builder()
                .email("someemail@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        trial(customer);

        // After a trial license is provided, make sure to check periodically/on every app restart that the license is still active.
        License license = manager.getCurrent();

        if (license == null) {
            // There aren't any active licenses on this device. Trial period ended.
            // User pays for the license.

            provideLicense();
        }

        // If licenses are based on consumption, add a consumption every time user starts the app.
        license.increaseConsumption();

        // if your product has multiple features, add a consumption every time certain feature is used.
        license.increaseFeatureConsumption("feature1");

        // This syncs local consumptions with server. Should be called periodically, or set up that it syncs automatically, see documentation.
        license.syncConsumptions();

        // Track variables which can be seen on the platform.
        ActivationLicense identity = ActivationLicense.fromUsername("username", "password");
        Map<String, String> variables = new HashMap<>();
        variables.put("variable1", "value1");
        variables.put("variable2", "value2");
        manager.trackVariables(identity, variables);

    }

    private static void trial(Customer customer) {
        try {
            UnactivatedTrialLicense trial = manager.getTrialLicense(customer);
            License license = manager.activateLicense(trial.createIdentity());
        } catch (LicenseSpringException e) {
            log.error("Something went wrong", e);
        }
    }

    private static void provideLicense() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // if your product is user based
            if (product.getAuthorizationMethod().equals("user")) {
                System.out.println("\nPlease enter username:");
                String username = reader.readLine();

                System.out.println("\nPlease enter password:");
                String password = reader.readLine();
                activateUser(username, password);
            } // if your product is license key based you can issue a license through Licensespring platform and send the key to the user via email.
            else {
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
