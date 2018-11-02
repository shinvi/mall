package com.shinvi.mall.pojo.domain;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItemDo {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productMainImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    public OrderItemDo(Integer id, Integer userId, Long orderNo, Integer productId, String productName, String productMainImage, BigDecimal currentUnitPrice, Integer quantity, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.orderNo = orderNo;
        this.productId = productId;
        this.productName = productName;
        this.productMainImage = productMainImage;
        this.currentUnitPrice = currentUnitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItemDo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage == null ? null : productMainImage.trim();
    }

    public BigDecimal getCurrentUnitPrice() {
        return currentUnitPrice;
    }

    public void setCurrentUnitPrice(BigDecimal currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}