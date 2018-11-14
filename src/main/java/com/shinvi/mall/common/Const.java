package com.shinvi.mall.common;

public interface Const {

    String ID = "id";
    String UPDATE_TIME = "update_time";

    interface OrderBy {
        String DESC = "desc";
        String ASC = "asc";

        String PRICE_DESC = "price_desc";
        String PRICE_ASC = "price_asc";
    }

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

        String PRICE = "price";

        int STATUS_IN_STOCK = 1;
        int STATUS_OUT_STOCK = 2;
        int STATUS_DELETE = 3;
    }

    interface Order {
        String ORDER_NO = "orderNo";

        int PAY_TYPE_ALIPAY = 1;
        int PAY_TYPE_WECHAT = 2;

        int STATUS_CANCELLED = 0;
        int STATUS_UNPAID = 10;
        int STATUS_PAID = 20;
        int STATUS_SHIPPED = 40;
        int STATUS_FINISHED = 50;
        int STATUS_CLOSED = 60;
    }

    interface AlipayCode {
        String SUCCESS = "10000";
        String UN_AVAILABLE = "20000";
        String MISS_PARAMS = "40001";
        String ILLEGAL_PARAMS = "40002";
        String FAILED = "40004";

        String SUB_ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";

        String QUERY_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String QUERY_TRADE_CLOSED = "TRADE_CLOSED";
        String QUERY_TRADE_SUCCESS = "TRADE_SUCCESS";
        String QUERY_TRADE_FINISHED = "TRADE_FINISHED";
    }
}
