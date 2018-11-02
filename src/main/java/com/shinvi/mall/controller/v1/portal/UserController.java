package com.shinvi.mall.controller.v1.portal;

import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.pojo.vo.UserVo;
import com.shinvi.mall.service.portal.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/users", headers = "version=1.0")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse<UserVo> login(String username, String password, HttpServletResponse response) {
        UserVo user = userService.login(username, password);
        Cookie cookie = new Cookie(Const.User.TOKEN, user.getToken());
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        return ServerResponse.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.User.CURRENT_USER);
        return ServerResponse.success();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse<UserDo> register(UserDo user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return ServerResponse.error("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return ServerResponse.error("密码不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            return ServerResponse.error("邮箱不能为空");
        }
        return ServerResponse.success(userService.register(user));
    }

    @RequestMapping(value = "/{" + Const.User.TOKEN + "}", method = RequestMethod.GET)
    public ServerResponse<UserDo> getUserByToken(@PathVariable(Const.User.TOKEN) String token) {
        UserDo user = userService.getUserInfo(token);
        if (user == null) {
            return ServerResponse.error("token无效或已过期");
        }
        return ServerResponse.success(user);
    }
}
