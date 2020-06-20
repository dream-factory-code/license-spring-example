package com.dreamfactory;

import com.licensespring.LicenseSpringConfiguration;
import lombok.Data;

@Data
public class Configuration {
    String apiKey;
    String productCode;
    String sharedKey;
    String appName;
    String appVer;
    String serviceURL;

    LicenseSpringConfiguration toLicenseSpringConfiguration() {
        return LicenseSpringConfiguration.builder()
                .apiKey(apiKey)
                .productCode(productCode)
                .sharedKey(sharedKey)
                .appName(appName)
                .appVersion(appVer)
                .serviceURL(serviceURL)
                .build();
    }
}
