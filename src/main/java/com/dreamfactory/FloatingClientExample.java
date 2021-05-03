package com.dreamfactory;

import com.licensespring.floating.FloatingConfiguration;
import com.licensespring.floating.FloatingLicenseService;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.DeviceVariables;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class FloatingClientExample {

    public static void main(String[] args) {
        // Initialization, every time app starts.
        String path = "sample.yaml";
        Yaml yaml = new Yaml(new Constructor(ConfigurationData.class));
        InputStream inputStream = LicenseClientExample.class.getClassLoader()
                .getResourceAsStream(path);

        ConfigurationData data = yaml.load(inputStream);

        FloatingConfiguration configuration = FloatingConfiguration.builder()
                .apiKey(data.getApiKey())
                .sharedKey(data.getSharedKey())
                .productCode(data.getProductCode())
                .appName(data.getAppName())
                .appVersion(data.getAppVer())
                .build();

        FloatingLicenseService service = new FloatingLicenseService(configuration);

        // Activate the license using users data.
        ActivationLicense identity = ActivationLicense.fromUsername("username", "password");

        service.activateLicense(identity);

        // If your product has licenses that are consumption type, add a consumptions.
        service.addConsumption(identity,1);

        // If your product has features, and has licenses that are consumption type, add a consumption to a certain feature.
        service.addFeatureConsumption(identity,"feature1", 1);

        // Track variables which can be seen on the platform.
        DeviceVariables deviceVariables = DeviceVariables.builder()
                .variable("variable1", "value1")
                .variable("variable2", "value2")
                .build();
        service.trackVariables(identity,  deviceVariables);

        // Upon exit or as needed, release the license.
        service.releaseLicense(identity);

    }

}
