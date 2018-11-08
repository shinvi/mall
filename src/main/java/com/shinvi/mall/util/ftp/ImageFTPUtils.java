package com.shinvi.mall.util.ftp;

import com.shinvi.mall.util.PropertiesUtils;


/**
 * @author 邱长海
 */
public class ImageFTPUtils extends BaseFTPUtils {
    private static final String FTP_IP = PropertiesUtils.getProperty("project.properties", "ftp.server.ip");
    private static final String FTP_USER = PropertiesUtils.getProperty("project.properties", "ftp.user");
    private static final String FTP_PASSWORD = PropertiesUtils.getProperty("project.properties", "ftp.password");

    private static final ThreadLocal<FTPUitls> THREAD_LOCAL = new ThreadLocal<>();

    private ImageFTPUtils() {
        super();
    }

    public static FTPUitls getCurrent() {
        FTPUitls ftpUitls = THREAD_LOCAL.get();
        if (ftpUitls == null) {
            ftpUitls = new ImageFTPUtils();
            THREAD_LOCAL.set(ftpUitls);
        }
        return ftpUitls;
    }

    @Override
    protected String getIp() {
        return FTP_IP;
    }

    @Override
    protected String getUser() {
        return FTP_USER;
    }

    @Override
    protected String getPassword() {
        return FTP_PASSWORD;
    }

    @Override
    protected String getDirectory() {
        return "image";
    }

    @Override
    protected void remove() {
        THREAD_LOCAL.remove();
    }

}
