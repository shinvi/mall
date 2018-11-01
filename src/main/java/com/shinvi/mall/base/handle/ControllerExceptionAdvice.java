package com.shinvi.mall.base.handle;

import com.github.pagehelper.util.StringUtil;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.pojo.vo.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse handleException(Exception e) {
        if (e instanceof ServerResponseException) {
            return ServerResponse.error(((ServerResponseException) e).getResponseCode());
        } else {
            return ServerResponse.error(StringUtils.isBlank(e.getMessage()) ? "请求失败" : e.getMessage());
        }
    }
}
