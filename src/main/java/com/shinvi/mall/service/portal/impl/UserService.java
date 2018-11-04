package com.shinvi.mall.service.portal.impl;

import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.UserDoMapper;
import com.shinvi.mall.dao.UserTokenDoMapper;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.domain.UserTokenDo;
import com.shinvi.mall.pojo.vo.UserVo;
import com.shinvi.mall.service.portal.IUserService;
import com.shinvi.mall.util.MD5Util;
import com.shinvi.mall.util.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDoMapper userDoMapper;

    @Autowired
    private UserTokenDoMapper userTokenDoMapper;

    @Transactional
    @Override
    public UserVo login(String username, String password, boolean single, boolean admin) {
        validPropertyExist(Const.User.USERNAME, username, false);
        UserDo user = userDoMapper.selectLogin(username, PasswordUtils.md5UserPassword(password));
        if (user == null) {
            throw new ServerResponseException("密码错误");
        }
        if (admin && user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new ServerResponseException("您不是系统管理员, 无法登录");
        }
        UserVo userVo = new UserVo(user);

        UserTokenDo userTokenDo = userTokenDoMapper.selectByUserId(userVo.getId());

        if (userTokenDo == null) {
            userTokenDo = new UserTokenDo();
            userTokenDo.setUserId(userVo.getId());
        }
        boolean expired = userTokenDo.getExpireTime() == null || userTokenDo.getExpireTime().getTime() < System.currentTimeMillis();
        if (expired || single) {
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
        validPropertyExist(Const.User.USERNAME, user.getUsername(), true);
        validPropertyExist(Const.User.EMAIL, user.getEmail(), true);
        user.setPassword(PasswordUtils.md5UserPassword(user.getPassword()));
        int resultCount = userDoMapper.insert(user);
        if (resultCount == 0) {
            throw new ServerResponseException("注册失败");
        }
        return user;
    }

    @Override
    public UserDo getUserById(Integer id) {
        return userDoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer getUserIdByToken(String token) {
        return userTokenDoMapper.selectUserIdByToken(token);
    }

    @Override
    public String getUserQuestionByUsername(String username) {
        validPropertyExist(Const.User.USERNAME, username, false);
        String question = userDoMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {
            throw new ServerResponseException(username + "没有设置密码安全问题");
        }
        return userDoMapper.selectQuestionByUsername(username);
    }

    @Transactional
    @Override
    public void restPasswordByQuestion(String username, String answer, String newPassword) {
        validPropertyExist(Const.User.USERNAME, username, false);
        int count = userDoMapper.updatePasswordByQuestion(username, answer, PasswordUtils.md5UserPassword(newPassword));
        if (count <= 0) {
            throw new ServerResponseException("安全问题答案回答错误");
        }
    }

    @Transactional
    @Override
    public void resetPasswordByOldPassword(Integer id, String oldPassword, String newPassword) {
        int count = userDoMapper.updatePasswordByOldPassword(id, PasswordUtils.md5UserPassword(oldPassword),
                PasswordUtils.md5UserPassword(newPassword));
        if (count <= 0) {
            throw new ServerResponseException("旧密码错误");
        }
    }

    @Transactional
    @Override
    public UserDo updateUserBySelf(UserDo user) {
        int emailCount = userDoMapper.countEmailWithoutPrimaryKey(user.getId());
        if (emailCount > 0) {
            throw new ServerResponseException("邮箱已存在");
        }
        user.setPassword(null);
        user.setRole(null);
        user.setUsername(null);
        int count = userDoMapper.updateByPrimaryKeySelective(user);
        if (count <= 0) {
            throw new ServerResponseException("更新失败");
        }
        return userDoMapper.selectByPrimaryKey(user.getId());
    }

    /**
     * 效验UserDo中某个属性的值是否存在于数据库表中
     *
     * @param property 属性名
     * @param content  值
     * @param exist    true代表效验已存在,false代表效验不存在
     */
    private void validPropertyExist(String property, String content, boolean exist) {
        int count;
        String message;
        switch (property) {
            case Const.User.EMAIL:
                count = userDoMapper.countEmail(content);
                message = exist ? "邮箱已存在" : "邮箱不存在";
                break;
            case Const.User.USERNAME:
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
