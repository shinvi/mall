package com.shinvi.mall.common;

/**
 * 接口响应结果常量
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "UNKNOW_ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

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
