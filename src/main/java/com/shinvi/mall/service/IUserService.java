package com.shinvi.mall.service;


import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.vo.UserVo;

public interface IUserService {

    /**
     * 普通用户登录
     *
     * @param username 用户名
     * @param password 密码明文
     * @param single   是否开启单登录(此用户在另外一处登录后,当前已登录的token即刻作废)
     * @param admin    true代表后台用户登录, false代表前台用户登录
     */
    UserVo login(String username, String password, boolean single, boolean admin);

    UserDo register(UserDo user);

    UserDo getUserById(Integer id);

    Integer getUserIdByToken(String token);

    UserDo getUserIdNRoleByToken(String token);

    String getUserQuestionByUsername(String username);

    void restPasswordByQuestion(String username, String answer, String newPassword);

    void resetPasswordByOldPassword(Integer id, String oldPassword, String newPassword);

    UserDo updateUserBySelf(UserDo user);
}
