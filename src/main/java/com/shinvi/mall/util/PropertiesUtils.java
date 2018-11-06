package com.shinvi.mall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 邱长海
 */
public class PropertiesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    public static String getProperty(String propertyFilename, String propertyKey) {
        Properties proerties = getProerties(propertyFilename);
        return proerties == null ? "" : proerties.getProperty(propertyKey.trim(), "").trim();
    }

    public static String getProperty(String propertyFilename, String propertyKey, String defaultValue) {
        Properties proerties = getProerties(propertyFilename);
        return proerties == null ? defaultValue : proerties.getProperty(propertyKey.trim(), defaultValue).trim();
    }

    private static Properties getProerties(String propertyFilename) {
        return ObjectUtils.autoClose(PropertiesUtils.class.getClassLoader().getResourceAsStream(propertyFilename.trim()),
                i -> {
                    Properties properties = new Properties();
                    try {
                        properties.load(i);
                    } catch (IOException e) {
                        LOGGER.error(propertyFilename + "配置加载失败 : " + e.getMessage());
                    }
                    return properties;
                });
    }
}
