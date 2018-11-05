package com.shinvi.mall.controller.v1.backend;

import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.pojo.vo.UserVo;
import com.shinvi.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "/manage/user", headers = "version=1.0")
public class UserManageController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse<UserVo> login(String username, String password, HttpServletResponse response) {
        UserVo user = userService.login(username, password, true, true);
        Cookie cookie = new Cookie(Const.User.USER_TOKEN, user.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        return ServerResponse.success(user);
    }
}
