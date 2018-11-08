package com.shinvi.mall.service.impl;

import com.shinvi.mall.service.IFileService;
import com.shinvi.mall.util.ftp.ImageFTPUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


/**
 * @author 邱长海
 */
@Service
public class FileService implements IFileService {
    @Override
    public String uploadImage(String parentPath, MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "." + file.getContentType().split("/")[1];
        return ImageFTPUtils.getCurrent().uploadFile(parentPath, fileName, file);
    }
}
