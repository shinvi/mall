package com.shinvi.mall.controller.v1.portal;

import com.google.common.collect.Lists;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IFileService;
import com.shinvi.mall.util.ftp.ImageFTPUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "/file", headers = "version=1.0")
public class FileUploadController {

    @Autowired
    private IFileService fileService;

    @ValidToken
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ServerResponse uploadImage(@RequestAttribute(Const.User.USER_ID) Integer userId, @RequestParam MultipartFile[] file) {
        StringBuilder errorMsgBuilder = new StringBuilder();
        List<String> successUris = Lists.newArrayList();
        for (int i = 0; i < file.length; i++) {
            MultipartFile multipartFile = file[i];
            if (multipartFile.getSize() > 3 * 8 * 1024 * 1024) {
                errorMsgBuilder.append("第" + (i + 1) + "张图片上传失败, 失败原因 : 图片大小必须小于3MB\n");
                continue;
            }
            if (!isPicture(multipartFile.getContentType())) {
                errorMsgBuilder.append("第" + (i + 1) + "张图片上传失败, 失败原因 : 不支持的图片类型\n");
                continue;
            }
            String uri = fileService.uploadImage(userId.toString(), multipartFile);
            if (StringUtils.isBlank(uri)) {
                errorMsgBuilder.append("第" + (i + 1) + "张图片上传失败, 失败原因 : 图片服务器连接失败\n");
                continue;
            }
            successUris.add(uri);
        }
        ImageFTPUtils.getCurrent().close();
        ServerResponse<List<String>> response = ServerResponse.success(successUris);
        response.setMsg(errorMsgBuilder.length() > 0 ? "共计" + (file.length - successUris.size()) + "张图片上传失败\n"
                + errorMsgBuilder.toString() : "上传成功");
        return response;
    }

    private boolean isPicture(String contentTpye) {
        String[] supportedContentTypes = {
                "image/bmp",
                "image/gif",
                "image/jpeg",
                "image/jpg",
                "image/png"};
        return Arrays.binarySearch(supportedContentTypes, contentTpye) >= 0;
    }
}
