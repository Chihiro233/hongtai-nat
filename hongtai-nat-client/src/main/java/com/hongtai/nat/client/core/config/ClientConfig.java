package com.hongtai.nat.client.core.config;


import java.util.Properties;

public class ClientConfig {

    private static final Properties properties;


    static {
        properties = ClientConfigInitializer.initializeConfig();
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

}
