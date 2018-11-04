package com.shinvi.mall.util;

import java.util.Properties;

/**
 * @author 邱长海
 */
public class PropertiesUtils {

    public static String getProperty(String propertyFilename, String propertyKey) {
        Properties proerties = getProerties(propertyFilename);
        return proerties == null ? "" : proerties.getProperty(propertyKey, "");
    }

    private static Properties getProerties(String propertyFilename) {
        return ObjectUtils.autoClose(PropertiesUtils.class.getClassLoader().getResourceAsStream(propertyFilename),
                i -> {
                    Properties properties = new Properties();
                    properties.load(i);
                    return properties;
                });
    }
}
