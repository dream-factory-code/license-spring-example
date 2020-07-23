package com.dreamfactory;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.hardware.HardwareInfo;
import com.licensespring.hardware.MachineInfo;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.Product;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        String path = "co.yaml";
        Yaml yaml = new Yaml(new Constructor(Configuration.class));
        InputStream inputStream = Main.class.getClassLoader()
                .getResourceAsStream(path);

        Configuration configuration = yaml.load(inputStream);

        LicenseManager manager = LicenseManager.getInstance();
        manager.initialize(configuration.toLicenseSpringConfiguration());

        Product product = manager.getProductDetails();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            if (product.getAuthorizationMethod().equals("user")) {
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

        License license = manager.getCurrent();

        MachineInfo machineInfo = HardwareInfo.getMachineInfo();
        String vmInfo = machineInfo.getVmInfo();

        System.out.println("Hardware ID >>> " + license.getHardwareId());
        System.out.println("VM info >>> " + vmInfo);

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        ComputerSystem computerSystem = hardware.getComputerSystem();

        String hardwareBase =  "BASE >> " + getBaseId(computerSystem.getBaseboard())
                + "\nCPU >> " + getCPU(hardware.getProcessor().getProcessorIdentifier())
                + "\nDISK >> " + getDiskId(hardware);
        System.out.println("Hardware >>> " + hardwareBase);

        System.exit(0);
    }

    private static void activateUser(String username, String password) {
        LicenseManager instance = LicenseManager.getInstance();

        License license = instance.activateLicense(ActivationLicense.fromUsername(username, password));
    }

    private static void activate(String key) {
        LicenseManager instance = LicenseManager.getInstance();

        License license = instance.activateLicense(ActivationLicense.fromKey(key));
    }


    private static String getDiskId(HardwareAbstractionLayer hardware) {
        List<HWDiskStore> diskStores = hardware.getDiskStores();
        if(diskStores == null || diskStores.size() == 0){
            return "";
        }

        HWDiskStore hwDiskStore = diskStores.get(0);
        return hwDiskStore.getModel() +
                hwDiskStore.getName() +
                hwDiskStore.getSerial();
    }

    private static String getBaseId(Baseboard baseboard) {
        return baseboard.getModel() +
                baseboard.getManufacturer() +
                baseboard.getSerialNumber() +
                baseboard.getVersion();
    }

    private static String getCPU(CentralProcessor.ProcessorIdentifier identifier){
        return identifier.getName() +
                identifier.getModel() +
                identifier.getProcessorID();
    }
}
