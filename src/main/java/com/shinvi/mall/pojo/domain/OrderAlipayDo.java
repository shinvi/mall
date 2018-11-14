package com.shinvi.mall.pojo.domain;

/**
 * @author 邱长海
 */
public class OrderAlipayDo {
    private Long orderNo;
    private String alipayTradeNo;
    private Integer n;

    public OrderAlipayDo(Long orderNo, String alipayTradeNo, Integer n) {
        this.orderNo = orderNo;
        this.alipayTradeNo = alipayTradeNo;
        this.n = n;
    }

    public OrderAlipayDo() {
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getAlipayTradeNo() {
        return alipayTradeNo;
    }

    public void setAlipayTradeNo(String alipayTradeNo) {
        this.alipayTradeNo = alipayTradeNo;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }
}
