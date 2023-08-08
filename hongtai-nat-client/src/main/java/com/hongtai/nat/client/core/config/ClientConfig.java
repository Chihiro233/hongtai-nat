package com.hongtai.nat.client.core.config;


import java.util.Objects;
import java.util.Properties;

public class ClientConfig {

    private static final Properties properties;


    static {
        properties = ClientConfigInitializer.initializeConfig();
    }

    public static String getStr(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInt(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }

}
