package org.igetwell.wechat.component.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.constans.WXPayConstants;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.*;
import org.igetwell.system.order.protocol.RefundPayCallProtocol;
import org.igetwell.wechat.component.service.ILocalReturnPayService;
import org.igetwell.wechat.sdk.api.MchPayAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalReturnPayService implements ILocalReturnPayService {

    private static final Logger logger = LoggerFactory.getLogger(LocalReturnPayService.class);

    @Value("${wechat.appId}")
    private String defaultAppId;

    @Value("${wechat.paterKey}")
    private String paterKey;

    @Value("${wechat.mchId}")
    private String mchId;

    @Value("${wechat.attach}")
    private String attach;

    @Value("${wechat.refundNotify}")
    private String refundNotify;

    @Value("${wechat.cert}")
    private String keyStoreFilePath;

    @Autowired
    private Sequence sequence;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 微信退款
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @throws Exception
     */
    public void returnPay(String transactionId, String tradeNo, String outNo, String totalFee, String fee) throws Exception {
        logger.info("[微信支付]-发起微信退款请求开始. 微信支付单号:{}, 商户单号, 退款金额：{}.", transactionId, tradeNo, fee);
        if (StringUtils.isBlank(transactionId) || StringUtils.isBlank(tradeNo) || StringUtils.isBlank(fee)) {
            logger.error("[微信支付]-发起微信退款失败. 请求参数为空. 微信支付单号:{}, 商户单号, 退款金额：{}.", transactionId, tradeNo, fee);
            throw new IllegalArgumentException("[微信支付]-发起微信退款失败. 请求参数为空.");
        }
        String nonceStr = sequence.nextNo();
        //String outNo = attach + sequence.nextValue();
        Map<String,String> paraMap= ParamMap.create("transactionId", transactionId)//微信订单号
                .put("tradeNo", tradeNo)//订单号
                .put("outNo", outNo) //退款单号
                .put("totalFee", totalFee)
                .put("fee", fee)
                .put("nonceStr", nonceStr)
                .getData();
        // 创建请求参数
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        logger.info("[微信支付]-微信退款调用开始. 请求微信退款参数：{}.", GsonUtils.toJson(params));
        //微信退款
        String result = this.refund(params);
        Map<String, String> resultXml = BeanUtils.xml2Map(result);
        String returnCode = resultXml.get("return_code");
        String resultCode = resultXml.get("result_code");
        boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode);
        if (!isSuccess) {
            logger.error("[微信支付]-微信退款失败!  商户订单号：{}, 微信支付订单号：{}.", transactionId, tradeNo);
            throw new RuntimeException("[微信支付]-微信退款失败." + resultXml.get("err_code_des"));
        }
        logger.info("[微信支付]-微信退款成功! 退款单号：{}, 商户订单号：{}, 微信支付订单号：{}.", outNo, tradeNo, transactionId);
    }

    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createParams(Map<String, String> hashMap, SignType signType) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("appid", defaultAppId);//appId
        params.put("mch_id", mchId);//商户号
        params.put("transaction_id", hashMap.get("transactionId"));//微信交易号
        params.put("out_trade_no", hashMap.get("tradeNo"));//商户订单号
        params.put("out_refund_no", hashMap.get("outNo"));//商户退款单号
        params.put("total_fee", hashMap.get("totalFee"));//订单金额，单位为分
        params.put("refund_fee", hashMap.get("fee"));//退款金额
        params.put("refund_fee_type", "CNY");//货币类型，默认人民币：CNY
        params.put("refund_desc", "用户申请退款");//退款原因
        params.put("nonce_str", hashMap.get("nonceStr"));//随机字符串，不长于32位。
        params.put("notify_url", refundNotify);//通知地址，接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

        String sign = SignUtils.createSign(params, paterKey, signType);
        params.put("sign", sign);

        return params;
    }

    /**
     * 处理微信退款回调
     * @param xmlStr
     * @return
     */
    public String notifyMethod(String xmlStr) {
        logger.info("[微信支付]-微信发起退款回调请求开始.");
        String successXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        String failXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[${return_msg}]]></return_msg></xml>";
        Map<String, String> resultXml = BeanUtils.xml2Map(xmlStr);
        String text = resultXml.get("req_info");
        xmlStr = AESDecodeUtil.decode(text, paterKey);
        Map<String, String> subXml = BeanUtils.xml2Map(xmlStr);
        String transactionId = subXml.get("transaction_id");//微信订单号
        String tradeNo = subXml.get("out_trade_no");//商户订单号
        String refundId = subXml.get("refund_id");//微信退款单号
        String outNo = subXml.get("out_refund_no");//商户退款单号
        String refundAccount = subXml.get("refund_recv_accout");//商户退款单号
        String status = subXml.get("refund_status");//退款状态：SUCCESS-退款成功CHANGE-退款异常REFUNDCLOSE—退款关闭
        String timestamp = subXml.get("success_time");//退款成功时间

        try {
            String returnCode = resultXml.get("return_code");
            boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(status);
            if (!isSuccess){
                //TODO:需要做数据库记录交易订单号
                logger.error("[微信支付]-微信退款失败! 商户订单号：{}, 微信支付订单号：{}, 商户退款号：{}, 微信退款号：{}, 退款成功时间: {}." , tradeNo, transactionId, outNo, refundId, timestamp);
                throw new RuntimeException("微信退款失败!");
            }
            RefundPayCallProtocol protocol = new RefundPayCallProtocol(transactionId, tradeNo, refundId, outNo, refundAccount, timestamp, status);
            //异步投递消息
            rocketMQTemplate.getProducer().setProducerGroup("refund-pay-order-call");
            rocketMQTemplate.asyncSend("refund-pay-order-call:refund-pay-order-call", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    logger.info("[微信支付]-退款订单修改订单状态异步消息投递成功,投递结果：{}.", var);
                }

                @Override
                public void onException(Throwable var) {
                    logger.error("[退款订单服务]-退款订单修改订单状态异步消息投递失败,异常信息：{}.", var);
                }

            });
            logger.info("[微信支付]-微信退款成功! 商户订单号：{}, 微信支付订单号：{}, 商户退款单号：{}, 微信退款单号：{}, 退款成功时间: {}." , tradeNo, transactionId, outNo, refundId, timestamp);
            return successXml;
        } catch (Exception e) {
            logger.error("[微信支付]-微信退款异常! 商户订单号：{}, 微信支付订单号：{}, 商户退款单号：{}, 微信退款单号：{}." , tradeNo, transactionId, outNo, refundId, timestamp, e);
            return failXml.replace("${return_msg}", "微信退款回调处理失败!");
        }

    }


    /**
     * 申请退款
     */
    private String refund(Map<String, String> params) throws Exception {
        return MchPayAPI.refund(mchId, keyStoreFilePath, BeanUtils.mapBean2Xml(params));
    }
}
