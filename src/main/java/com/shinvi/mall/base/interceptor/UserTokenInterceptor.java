package com.shinvi.mall.base.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.common.ResponseCode;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IUserService;
import com.shinvi.mall.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 邱长海
 * 负责拦截需要用户信息的接口,
 */
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod) || !((HandlerMethod) handler).hasMethodAnnotation(ValidToken.class)) {
            return true;
        }

        Integer userId = userService.getUserIdByToken(UserUtils.findUserTokenByRequest(request));
        if (userId == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(ServerResponse.error(ResponseCode.NEED_LOGIN)));
            return false;
        }
        request.setAttribute(Const.User.USER_ID, userId);
        return true;
    }
}
