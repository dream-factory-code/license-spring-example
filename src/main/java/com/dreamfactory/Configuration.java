package com.dreamfactory;

import com.licensespring.LicenseSpringConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Configuration {
    private String apiKey;
    private String productCode;
    private String sharedKey;

    private String appName;
    private String appVersion;

    private String serviceURL = "https://api.licensespring.com/";

    LicenseSpringConfiguration toLicenseSpringConfiguration(){
        return LicenseSpringConfiguration.builder()
                .apiKey(apiKey)
                .productCode(productCode)
                .sharedKey(sharedKey)
                .appName(appName)
                .appVersion(appVersion)
                .serviceURL(serviceURL)
                .build();
    }
}
