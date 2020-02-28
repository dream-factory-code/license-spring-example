package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.dto.UnactivatedTrialLicense;
import com.licensespring.model.ActivationLicense;

public class Main {

    public static void main(String[] args) {

        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey("api-key")
                .productCode("TT")
                .sharedKey("shared-secret_key")
                .appName("Test app")
                .appVersion("3")
                .build();

        LicenseManager manager = LicenseManager.getInstance();

        manager.initialize(configuration);

        trial();

        activate();

        activateUser();

    }

    private static void activateUser() {
        LicenseManager instance = LicenseManager.getInstance();
        License license = instance.activateLicense(ActivationLicense.fromUsername("test", "pswd"));
        instance.deactivateLicense(license.getIdentity());
    }

    private static void activate() {
        LicenseManager instance = LicenseManager.getInstance();
        License license = instance.activateLicense(ActivationLicense.fromKey("key"));
        instance.deactivateLicense(license.getIdentity());
    }

    private static void trial(){
        LicenseManager instance = LicenseManager.getInstance();
        UnactivatedTrialLicense trialLicense = instance.getTrialLicense("test@test.com");
        License license = instance.activateLicense(trialLicense.createIdentity());

        assert instance.getCurrent().equals(license);
    }
}
