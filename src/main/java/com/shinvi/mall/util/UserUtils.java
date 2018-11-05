package com.shinvi.mall.util;

import com.shinvi.mall.common.Const;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class UserUtils {

    public static String findUserTokenByRequest(HttpServletRequest request) {
        String token = request.getHeader(Const.User.USER_TOKEN);
        if (StringUtils.isBlank(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Const.User.USER_TOKEN.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }
}
