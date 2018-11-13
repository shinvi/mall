package com.shinvi.mall.controller.v1.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.vo.AlipayOrderVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "pay", headers = "version=1.0")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ValidToken
    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    public ServerResponse<String> alipayQrcodeOrder(@RequestAttribute(Const.User.USER_ID) Integer userId) throws JsonProcessingException {
        AlipayTradePrecreateRequest request = (AlipayTradePrecreateRequest) applicationContext.getBean("alipayRequest");
        AlipayOrderVo alipayOrder = new AlipayOrderVo("12599", "iphone xs max 256g");
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+ UUID.randomUUID().toString() +"\"," +
                "    \"total_amount\":\"12599\"," +
                "    \"subject\":\"iphone xs max 256g\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"2m\"," +
                "  }");
        try {
            AlipayTradePrecreateResponse execute = alipayClient.execute(request);
            return ServerResponse.success(execute.getBody());
        } catch (AlipayApiException e) {
            logger.error(e.getErrCode());
            logger.error(e.getErrMsg());
            throw new ServerResponseException("支付宝下单失败,请5秒后重试");
        }
    }
}
