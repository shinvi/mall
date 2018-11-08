package com.shinvi.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 邱长海
 */
public interface IFileService {

    String uploadImage(String parentPath, MultipartFile file);
}
