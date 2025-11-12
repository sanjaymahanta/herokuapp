package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.baseclass.DriverManager;

public class Util {

    public static String properties(String fileName, String key) {
        Properties props = new Properties();

        // Try multiple locations for flexibility
        String[] possiblePaths = {
            "AppConfig/" + fileName + ".properties",
            "src/test/resources/AppConfig/" + fileName + ".properties",
            "./" + fileName + ".properties"
        };

        File found = null;
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                found = f;
                break;
            }
        }

        if (found == null) {
            throw new RuntimeException(" Could not find " + fileName + ".properties in any known path.");
        }

        System.out.println(" Loading config from: " + found.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(found)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(" Failed to load: " + found.getAbsolutePath(), e);
        }

        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("⚠ Missing property '" + key + "' in file: " + found.getAbsolutePath());
        }

        value = value.trim();
        System.out.println("🔹 " + key + " = " + value);
        return value;
    }

    public static byte[] takeScreenShot() {
        WebDriver driver = DriverManager.webDriver.get();
        if (driver == null) return new byte[0];
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
