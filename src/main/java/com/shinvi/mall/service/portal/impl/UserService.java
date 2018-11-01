package com.shinvi.mall.service.portal.impl;

import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.UserDoMapper;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.service.portal.IUserService;
import com.shinvi.mall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Override
    public UserDo login(String username, String password) {
        validPropertyExist(Const.User.PROPERTY_USERNAME, username, false);
        
        UserDo user = userDoMapper.selectLogin(username, MD5Util.getMD5(password));
        if (user == null) {
            throw new ServerResponseException("密码错误");
        }
        user.setPassword("");
        return user;
    }

    @Transactional
    @Override
    public UserDo register(UserDo user) {
        validPropertyExist(Const.User.PROPERTY_USERNAME, user.getUsername(), true);
        validPropertyExist(Const.User.PROPERTY_EMAIL, user.getEmail(), true);

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        int resultCount = userDoMapper.insert(user);
        if (resultCount == 0) {
            throw new ServerResponseException("注册失败");
        }
        return user;
    }

    private void validPropertyExist(String property, String content, boolean exist) {
        int count;
        String message;
        switch (property) {
            case Const.User.PROPERTY_EMAIL:
                count = userDoMapper.countEmail(content);
                message = exist ? "用户名已存在" : "用户名不存在";
                break;
            case Const.User.CURRENT_USER:
                count = userDoMapper.countUsername(content);
                message = exist ? "邮箱已存在" : "邮箱不存在";
                break;
            default:
                return;
        }
        if (exist && count > 0 || !exist && count == 0) {
            throw new ServerResponseException(message);
        }
    }
}
