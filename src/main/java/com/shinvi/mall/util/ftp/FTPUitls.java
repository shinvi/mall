package com.shinvi.mall.util.ftp;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author 邱长海
 */
public interface FTPUitls {

    String uploadFile(String directoryName, String fileName, File tempFile);

    String uploadFile(String directoryName, String fileName, MultipartFile tempFile);

    void close();
}
