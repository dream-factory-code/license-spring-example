package com.dreamfactory;

import com.licensespring.floating.FloatingConfiguration;
import com.licensespring.floating.FloatingLicenseService;
import com.licensespring.model.ActivationLicense;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FloatingClientExample {

    public static void main(String[] args) {
        // Initialization, every time app starts.
        String path = "sample.yaml";
        Yaml yaml = new Yaml(new Constructor(FloatingConfiguration.class));
        InputStream inputStream = LicenseClientExample.class.getClassLoader()
                .getResourceAsStream(path);

        FloatingConfiguration configuration = yaml.load(inputStream);

        configuration.setCloudFloating(true);

        FloatingLicenseService service = new FloatingLicenseService(configuration);

        // Activate the license using users data.
        ActivationLicense identity = ActivationLicense.fromUsername("username", "password");

        service.activateLicense(identity);

        // If your product has licenses that are consumption type, add a consumptions.
        service.addConsumption(identity,1);

        // If your product has features, and has licenses that are consumption type, add a consumption to a certain feature.
        service.addFeatureConsumption(identity,"feature1", 1);

        // Track variables which can be seen on the platform.
        Map<String, String> variables = new HashMap<>();
        variables.put("variable1", "value1");
        variables.put("variable2", "value2");
        service.trackVariables(identity, variables);

        // Upon exit or as needed, release the license.
        service.releaseLicense(identity);

    }

}
