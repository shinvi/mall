package com.shinvi.mall.service.portal;


import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.vo.UserVo;

public interface IUserService {

    UserVo login(String username, String password);

    UserDo register(UserDo user);

    UserDo getUserInfo(String token);
}
