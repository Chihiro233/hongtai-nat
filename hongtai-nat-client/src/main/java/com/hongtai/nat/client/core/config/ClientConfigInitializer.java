package com.hongtai.nat.client.core.config;

import com.hongtai.nat.client.core.exception.ConfigFileNotExistException;
import com.hongtai.nat.client.core.exception.LoadConfigFailException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class ClientConfigInitializer {

    private static String CLIENT_CONFIG_KEY = "client-config";

    private static String CONFIG_PATH = "/config/client.config";


    public static Properties initializeConfig() {
        Properties config = new Properties();

        String pathValue = System.getProperties().getProperty(CLIENT_CONFIG_KEY);
        File configFile = StringUtils.isBlank(pathValue) ? new File(CONFIG_PATH) : new File(pathValue);
        if (!configFile.exists() || !configFile.isFile()) {
            throw new ConfigFileNotExistException("config file is not exist");
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(configFile));
            config.load(inputStreamReader);
            log.info("load config complete");
            return config;
        } catch (Exception e) {
            log.error("load config fail ", e);
            throw new LoadConfigFailException("load config fail");
        }


    }

}
