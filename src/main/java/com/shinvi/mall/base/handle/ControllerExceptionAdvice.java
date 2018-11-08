package com.shinvi.mall.base.handle;

import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.pojo.vo.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionAdvice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse handleException(Exception e) {
        if (e instanceof ServerResponseException) {
            return ServerResponse.error(((ServerResponseException) e).getResponseCode());
        } else {
            logger.error("Controller异常 : " + e.getMessage());
            e.printStackTrace();
            return ServerResponse.exception(e);
        }
    }
}
