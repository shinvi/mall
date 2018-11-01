package com.shinvi.mall.common;

public interface Const {
    interface User {
        String CURRENT_USER = "current_user";

        String PROPERTY_EMAIL = "email";

        String PROPERTY_USERNAME = "username";
    }

    interface Role {
        /**
         * 普通用户
         */
        int ROLE_CUSTOMER = 0;

        /**
         * 管理员
         */
        int ROLE_ADMIN = 1;
    }
}
