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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }


}
