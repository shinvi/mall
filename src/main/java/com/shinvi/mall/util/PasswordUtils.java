package com.shinvi.mall.util;

/**
 * @author 邱长海
 */
public class PasswordUtils {

    public static String md5UserPassword(String password) {
        return MD5Util.getMD5(password
                + PropertiesUtils.getProperty("project.properties", "user.password.salt"));
    }
}
