package com.shinvi.mall.pojo.domain;

import java.util.Date;

public class UserTokenDo {
    private Integer id;

    private Integer userId;

    private Date expireTime;

    private String token;

    public UserTokenDo(Integer id, Integer userId, Date expireTime, String token) {
        this.id = id;
        this.userId = userId;
        this.expireTime = expireTime;
        this.token = token;
    }

    public UserTokenDo() {
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }
}