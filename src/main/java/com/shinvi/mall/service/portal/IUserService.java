package com.shinvi.mall.service.portal;


import com.shinvi.mall.pojo.domain.UserDo;

public interface IUserService {

    UserDo login(String username, String password);

    UserDo register(UserDo user);
}
