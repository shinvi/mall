package com.shinvi.mall.pojo.vo;

/**
 * @author 邱长海
 */
public class QrCodeOrderVo {
    private String qrCode;
    private String timeout;
    private Integer payType;
    private Long orderNo;
    private String outTradeNo;

    public QrCodeOrderVo(String qrCode, String timeout, Integer payType, Long orderNo, String outTradeNo) {
        this.qrCode = qrCode;
        this.timeout = timeout;
        this.payType = payType;
        this.orderNo = orderNo;
        this.outTradeNo = outTradeNo;
    }

    public QrCodeOrderVo() {
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
