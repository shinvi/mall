package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.pojo.domain.UserDo;
import org.springframework.beans.BeanUtils;


public class UserVo extends UserDo {

    public UserVo() {
    }

    public UserVo(UserDo user) {
        BeanUtils.copyProperties(user, this);
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
