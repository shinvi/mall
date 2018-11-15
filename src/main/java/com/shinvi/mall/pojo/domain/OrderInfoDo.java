package com.shinvi.mall.pojo.domain;

/**
 * @author 邱长海
 */
public class OrderInfoDo {
    private Long orderNo;
    private String outTradeNo;
    private Integer payType;
    private Integer n;

    public OrderInfoDo(Long orderNo, String outTradeNo, Integer payType, Integer n) {
        this.orderNo = orderNo;
        this.outTradeNo = outTradeNo;
        this.payType = payType;
        this.n = n;
    }

    public OrderInfoDo(Long orderNo, Integer payType) {
        this.orderNo = orderNo;
        this.payType = payType;
    }

    public OrderInfoDo() {
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
