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
        String OUT_TRADE_NO = "outTradeNo";

        int PAY_TYPE_ALIPAY = 1;
        int PAY_TYPE_WECHAT = 2;

        /**
         * 订单已取消
         */
        int STATUS_CANCELLED = 0;
        /**
         * 订单未支付
         */
        int STATUS_UNPAID = 10;
        /**
         * 订单已支付
         */
        int STATUS_PAID = 20;
        /**
         * 订单已发货
         */
        int STATUS_SHIPPED = 40;
        /**
         * 订单已完成
         */
        int STATUS_FINISHED = 50;
        /**
         * 订单已关闭
         */
        int STATUS_CLOSED = 60;
    }

    interface AlipayCode {
        /**
         * 成功
         */
        String SUCCESS = "10000";
        /**
         * 服务不存在
         */
        String UN_AVAILABLE = "20000";
        /**
         * 缺少必传参数
         */
        String MISS_PARAMS = "40001";
        /**
         * 参数非法
         */
        String ILLEGAL_PARAMS = "40002";
        /**
         * 失败
         */
        String FAILED = "40004";

        /**
         * query结果subCode该流水号不存在
         */
        String SUB_ACQ_TRADE_NOT_EXIST = "ACQ.TRADE_NOT_EXIST";

        /**
         * 等待付款
         */
        String QUERY_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        /**
         * 交易已关闭(包含撤消和退款)
         */
        String QUERY_TRADE_CLOSED = "TRADE_CLOSED";
        /**
         * 支付成功(可退款)
         */
        String QUERY_TRADE_SUCCESS = "TRADE_SUCCESS";
        /**
         * 支付完成(不可退款)
         */
        String QUERY_TRADE_FINISHED = "TRADE_FINISHED";
    }
}
