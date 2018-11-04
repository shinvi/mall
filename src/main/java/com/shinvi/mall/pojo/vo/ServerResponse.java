package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.common.ResponseCode;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
    public int status;
    private String msg;
    private Exception exception;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public static <T> ServerResponse<T> success(T data) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setStatus(ResponseCode.SUCCESS.getCode());
        response.setMsg(ResponseCode.SUCCESS.getDesc());
        response.setData(data);
        return response;
    }

    public static <T> ServerResponse<T> success() {
        ServerResponse<T> response = new ServerResponse<>();
        response.setStatus(ResponseCode.SUCCESS.getCode());
        response.setMsg(ResponseCode.SUCCESS.getDesc());
        return response;
    }

    public static <T> ServerResponse<T> error(ResponseCode responseCode) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setStatus(responseCode.getCode());
        response.setMsg(responseCode.getDesc());
        return response;
    }

    public static <T> ServerResponse<T> error(String msg) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setStatus(ResponseCode.ERROR.getCode());
        response.setMsg(msg);
        return response;
    }

    public static <T> ServerResponse<T> exception(Exception e) {
        ServerResponse<T> response = new ServerResponse<>();
        response.setStatus(ResponseCode.SERVER_ERROR.getCode());
        response.setMsg(ResponseCode.SERVER_ERROR.getDesc());
        response.setException(e);
        return response;
    }
}
