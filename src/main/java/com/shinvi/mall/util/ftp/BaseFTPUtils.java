package com.shinvi.mall.util.ftp;


import javafx.util.Pair;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * @author 邱长海
 */
public abstract class BaseFTPUtils implements FTPUitls {
    private FTPClient ftpClient;

    BaseFTPUtils() {
        initFTPClient();
    }

    @Override
    public String uploadFile(String directoryName, String fileName, File tempFile) {
        try (FileInputStream inputStream = new FileInputStream(tempFile)) {
            return upload(directoryName, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String uploadFile(String directoryName, String fileName, MultipartFile tempFile) {
        try (InputStream inputStream = tempFile.getInputStream()) {
            return upload(directoryName, fileName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String upload(String directoryName, String fileName, InputStream inputStream) throws IOException {
        Pair<String, String[]> ftpDirectory = createFtpDirectory(directoryName);
        String directorys = ftpDirectory.getKey();
        boolean change = ftpClient.changeWorkingDirectory(directorys);
        if (!change) {
            StringBuilder directoryBuilder = new StringBuilder();
            for (String directory : ftpDirectory.getValue()) {
                directoryBuilder.append(directory).append(File.separator);
                ftpClient.makeDirectory(directoryBuilder.toString());
            }
        }

        change = change || ftpClient.changeWorkingDirectory(directorys);
        if (!change) {
            return null;
        }
        boolean success = ftpClient.storeFile(fileName, inputStream);
        return success ? directorys + File.separator + fileName : null;
    }

    @Override
    public void close() {
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
                remove();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initFTPClient() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(getIp());
            ftpClient.login(getUser(), getPassword());
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pair<String, String[]> createFtpDirectory(String parent) {
        Calendar calendar = Calendar.getInstance();
        return new Pair<>(getDirectory() + File.separator + parent + File.separator + calendar.get(Calendar.YEAR) +
                calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH),
                new String[]{getDirectory(), parent, String.valueOf(calendar.get(Calendar.YEAR)) +
                        calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH)});
    }

    protected abstract String getIp();

    protected abstract String getUser();

    protected abstract String getPassword();

    protected abstract String getDirectory();

    protected abstract void remove();
}
