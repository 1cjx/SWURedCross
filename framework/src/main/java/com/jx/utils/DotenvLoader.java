package com.jx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DotenvLoader {

    public static void loadDotenv() {
        try (InputStream input = DotenvLoader.class.getClassLoader().getResourceAsStream(".env")) {
            if (input == null) {
                throw new RuntimeException(".env file not found in resources");
            }

            Properties prop = new Properties();
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                System.setProperty(key, value);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not load .env file", ex);
        }
    }
}
