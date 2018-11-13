package com.shinvi.mall.pojo.vo;

import java.util.UUID;

/**
 * @author 邱长海
 */
public class AlipayOrderVo {
    /**
     * 商户订单号，需要保证不重复
     */
    private String out_trade_no;
    /**
     * 订单金额
     */
    private String total_amount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商户门店编号
     */
    private String store_id;

    /**
     * 交易超时时间
     */
    private String timeout_express;

    public AlipayOrderVo(String out_trade_no, String total_amount, String subject, String store_id, String timeout_express) {
        this.out_trade_no = out_trade_no;
        this.total_amount = total_amount;
        this.subject = subject;
        this.store_id = store_id;
        this.timeout_express = timeout_express;
    }

    public AlipayOrderVo(String out_trade_no, String total_amount, String subject, String store_id) {
        this(out_trade_no, total_amount, subject, store_id, "90m");
    }

    public AlipayOrderVo(String total_amount, String subject, String store_id) {
        this(UUID.randomUUID().toString(), total_amount, subject, store_id, "90m");
    }

    public AlipayOrderVo(String total_amount, String subject) {
        this(UUID.randomUUID().toString(), total_amount, subject, "N1", "90m");
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }
}
