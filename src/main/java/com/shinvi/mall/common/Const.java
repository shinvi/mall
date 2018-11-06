package com.shinvi.mall.common;

public interface Const {

    String ID = "id";

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

    interface Category {
        String NAME = "name";
    }

    interface Product {
        String CATEGORY_ID = "categoryId";

        int STATUS_IN_STOCK = 1;
        int STATUS_OUT_STOCK = 2;
        int STATUS_DELETE = 3;
    }
}
