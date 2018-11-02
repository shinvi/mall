package com.shinvi.mall.service.portal.impl;

import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.UserDoMapper;
import com.shinvi.mall.dao.UserTokenDoMapper;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.domain.UserTokenDo;
import com.shinvi.mall.pojo.vo.UserVo;
import com.shinvi.mall.service.portal.IUserService;
import com.shinvi.mall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserTokenDoMapper userTokenDoMapper;

    @Transactional
    @Override
    public UserVo login(String username, String password) {
        validPropertyExist(Const.User.PROPERTY_USERNAME, username, false);
        UserDo user = userDoMapper.selectLogin(username, MD5Util.getMD5(password));
        if (user == null) {
            throw new ServerResponseException("密码错误");
        }
        UserVo userVo = new UserVo(user);

        UserTokenDo userTokenDo = userTokenDoMapper.selectByUserId(userVo.getId());
        if (userTokenDo == null) {
            userTokenDo = new UserTokenDo();
            userTokenDo.setUserId(userVo.getId());
        }
        if (userTokenDo.getExpireTime() == null || userTokenDo.getExpireTime().getTime() < System.currentTimeMillis()) {
            long exPireTime = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30);
            userTokenDo.setExpireTime(new Date(exPireTime));
            userTokenDo.setToken(MD5Util.getMD5(MD5Util.getMD5(userTokenDo.getUserId().toString()) + MD5Util.getMD5(String.valueOf(exPireTime))));
            if (userTokenDo.getId() != null) {
                userTokenDoMapper.updateByPrimaryKeySelective(userTokenDo);
            } else {
                userTokenDoMapper.insert(userTokenDo);
            }
        }
        userVo.setToken(userTokenDo.getToken());
        return userVo;
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

    @Override
    public UserDo getUserInfo(String token) {
        return userDoMapper.selectByToken(token);
    }

    private void validPropertyExist(String property, String content, boolean exist) {
        int count;
        String message;
        switch (property) {
            case Const.User.PROPERTY_EMAIL:
                count = userDoMapper.countEmail(content);
                message = exist ? "邮箱已存在" : "邮箱不存在";
                break;
            case Const.User.PROPERTY_USERNAME:
                count = userDoMapper.countUsername(content);
                message = exist ? "用户名已存在" : "用户名不存在";
                break;
            default:
                return;
        }
        if (exist && count > 0 || !exist && count == 0) {
            throw new ServerResponseException(message);
        }
    }
}
