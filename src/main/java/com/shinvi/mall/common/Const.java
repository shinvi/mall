package com.shinvi.mall.common;

public interface Const {
    interface User {
        String USER_TOKEN = "user_token";

        String EMAIL = "email";

        String USERNAME = "username";

        String USER_ID = "userId";
    }

    interface Role {
        /**
         * 管理员
         */
        int ROLE_ADMIN = 0;
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 1;
    }
}
