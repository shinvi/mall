package com.shinvi.mall.common;

/**
 * 接口响应结果常量
 */
public enum ResponseCode {
    SUCCESS(0, "请求成功"),
    ERROR(1, "未知错误"),
    NEED_LOGIN(10, "您的登录已过期，请重新登录"),
    ILLEGAL_ARGUMENT(2, "非法的请求参数");

    private final int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public ResponseCode setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}