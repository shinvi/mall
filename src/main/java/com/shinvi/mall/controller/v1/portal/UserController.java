package com.shinvi.mall.controller.v1.portal;

import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.pojo.vo.UserVo;
import com.shinvi.mall.service.IUserService;
import com.shinvi.mall.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 邱长海
 */

@RestController
@RequestMapping(value = "/user", headers = "version=1.0")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse<UserVo> login(String username, String password, HttpServletResponse response) {
        UserVo user = userService.login(username, password, false, false);
        Cookie cookie = new Cookie(Const.User.USER_TOKEN, user.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        return ServerResponse.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ServerResponse logout(@CookieValue(Const.User.USER_TOKEN) String token, HttpServletResponse response) {
        response.addCookie(ObjectUtils.with(new Cookie(Const.User.USER_TOKEN, token), cookie -> cookie.setMaxAge(0)));
        return ServerResponse.success();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<UserVo> register(UserDo user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return ServerResponse.error("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return ServerResponse.error("密码不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return ServerResponse.error("邮箱不能为空");
        }
        if (StringUtils.isBlank(user.getPwQuestion())) {
            return ServerResponse.error("密码问题不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return ServerResponse.error("密码答案不能为空");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        return ServerResponse.success(new UserVo(userService.register(user)));
    }

    @ValidToken
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ServerResponse<UserVo> getUserByToken(@RequestAttribute(Const.User.USER_ID) Integer userId) {
        UserDo user = userService.getUserById(userId);
        if (user == null) {
            return ServerResponse.error("token无效或已过期");
        }
        return ServerResponse.success(new UserVo(user));
    }

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public ServerResponse<String> getUserQuestion(String username) {
        return ServerResponse.success(userService.getUserQuestionByUsername(username));
    }

    @RequestMapping(value = "/question/password", method = RequestMethod.PUT)
    public ServerResponse restPasswordByQuestion(String username, String answer, String newPassword) {
        if (StringUtils.isBlank(newPassword)) {
            return ServerResponse.error("新密码不能为空");
        }
        userService.restPasswordByQuestion(username, answer, newPassword);
        return ServerResponse.success();
    }

    @ValidToken
    @RequestMapping(value = "/password", method = RequestMethod.PUT)
    public ServerResponse resetPasswordByOldPassword(@RequestAttribute(Const.User.USER_ID) Integer userId, String oldPassword,
                                        String newPassword) {
        if (StringUtils.isBlank(newPassword)) {
            return ServerResponse.error("新密码不能为空");
        }
        userService.resetPasswordByOldPassword(userId, oldPassword, newPassword);
        return ServerResponse.success();
    }

    @ValidToken
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse<UserVo> updateUser(@RequestAttribute(Const.User.USER_ID) Integer userId, UserDo user) {
        user.setId(userId);
        return ServerResponse.success(new UserVo(userService.updateUserBySelf(user)));
    }
}
