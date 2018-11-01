package com.shinvi.mall.controller.portal;

import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.UserDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.portal.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse<UserDo> login(String username, String password, HttpSession httpSession) {
        UserDo user = userService.login(username, password);
        if (user != null) {
            httpSession.setAttribute(Const.User.CURRENT_USER, user);
        }
        return ServerResponse.success(user);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.User.CURRENT_USER);
        return ServerResponse.success();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ServerResponse<UserDo> register(UserDo user) {
        return ServerResponse.success(userService.register(user));
    }
}
