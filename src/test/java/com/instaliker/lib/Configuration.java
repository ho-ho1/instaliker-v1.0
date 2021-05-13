package com.instaliker.lib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    static Properties properties;

    public static Properties properties() {
        if (properties == null) {
            properties = (new Configuration()).load("config.properties");
        }
        return properties;
    }

    public Properties load(String filename) {
        Properties properties = new Properties();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Could not load settings file.");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            boolean isLoaded = Boolean.parseBoolean((String) properties.get("login.username"));
            if (isLoaded) {
                System.out.println("Settings file successfully loaded");
            }
        }
        return properties;
    }
}
