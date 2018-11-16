package com.shinvi.mall.common;

/**
 * 接口响应结果常量
 */
public enum ResponseCode {
    SUCCESS(0, "请求成功"),
    ERROR(1, "未知错误"),
    SERVER_ERROR(5, "请求出错"),
    NEED_LOGIN(10, "您的登录已过期，请重新登录"),
    PERMISSION_ERROR(6, "您暂无此项操作的权限"),
    ILLEGAL_ARGUMENT(2, "非法的请求参数"),
    PAY_SUCCEDD(30, "该订单交易已完成"),
    PAY_STATUS_EXCEPTION(31, "订单支付出现异常状况,请联系工作人员处理"),
    PAY_STATUS_FAILED(32, "订单支付状态查询失败,请稍后再重新查询"),
    PAY_STATUS_NOT_EXIST(33, "该订单支付尚未开始,请稍后再重新查询"),
    PAY_STATUS_WATTING(34, "该订单支付正在进行中,请稍后再重新查询");

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
