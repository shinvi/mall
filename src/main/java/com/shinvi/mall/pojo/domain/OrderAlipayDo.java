package com.shinvi.mall.pojo.domain;

/**
 * @author 邱长海
 */
public class OrderAlipayDo {
    private Long orderNo;
    private String alipayTradeNo;
    private Integer index;

    public OrderAlipayDo(Long orderNo, String alipayTradeNo, Integer index) {
        this.orderNo = orderNo;
        this.alipayTradeNo = alipayTradeNo;
        this.index = index;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
