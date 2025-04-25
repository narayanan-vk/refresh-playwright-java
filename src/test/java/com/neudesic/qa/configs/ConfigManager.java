package com.neudesic.qa.configs;

import com.microsoft.playwright.junit.Options;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class ConfigManager {
    private static Config config;

    static {
        try {
            System.setProperty("config.resource", "application.conf");
            config = ConfigFactory.load();
            System.out.println("Loaded configuration: " + config.getConfig("test"));
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            config = ConfigFactory.empty();
        }
    }

    public static String getBrowser() {
        return getString("test.browser");
    }

    public static String getServer() {
        return getString("test.server");
    }

    public static boolean isHeadless() {
        return getBoolean("test.headless");
    }

    public static boolean isCi() {
        return getBoolean("test.ci");
    }

    public static String getBaseUrl() {
        return getString("test.baseUrl");
    }

    public static Options.Trace getTrace(){
        return Options.Trace.valueOf(getString("test.trace"));
    }

    private static String getString(String path) {
        // Check environment variable first (with proper naming conversion)
        String envKey = path.replace('.', '_').toUpperCase();
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // Fall back to configuration file
        return config.getString(path);
    }

    private static boolean getBoolean(String path) {
        String envKey = path.replace('.', '_').toUpperCase();
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isEmpty()) {
            return Boolean.parseBoolean(envValue);
        }

        return config.getBoolean(path);
    }
}
