package org.igetwell.wechat.component.service.impl;

import org.igetwell.common.constans.WXPayConstants;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.enums.TradeType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.*;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.feign.TradeOrderClient;
import org.igetwell.wechat.component.service.ILocalPayService;
import org.igetwell.wechat.sdk.api.MchPayAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalPayService implements ILocalPayService {

    private static final Logger logger = LoggerFactory.getLogger(LocalPayService.class);

    @Value("${wechat.appId}")
    private String defaultAppId;

    @Value("${wechat.paterKey}")
    private String paterKey;

    @Value("${wechat.mchId}")
    private String mchId;

    @Value("${attach}")
    private String attach;

    @Value("${wechat.payNotify}")
    private String payNotify;

    @Autowired
    private Sequence sequence;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TradeOrderClient tradeOrderClient;


    /**
     * 扫码预付款下单
     * @param request
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    public String scanOrder(HttpServletRequest request, String productName, String productId, String fee) {
        String nonceStr = sequence.nextNo();
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String tradeNo = attach + sequence.nextValue();

        try {
            String clientIp = WebUtils.getIP(request);
            Map<String,String> paraMap= ParamMap.create("productId", productId)
                    .put("body", productName)
                    .put("tradeNo", tradeNo)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .put("timestamp",timestamp)
                    .put("clientIp", clientIp)
                    .put("tradeType", "NATIVE")
                    .getData();

            Map<String, String> resultXml = this.prePay(paraMap, SignType.MD5);
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");
            String codeUrl = resultXml.get("code_url");
            boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode);
            if (!isSuccess) {
                throw new RuntimeException("统一下单失败！" +
                        "return_code:" + resultXml.get("return_code") + " " +
                        "return_msg:" + resultXml.get("return_msg"));
            }
            logger.info("统一下单调用结束！预支付交易标识：{}. {}", resultXml.get("prepay_id"), resultXml.get("return_msg"));

            TradeOrder order = new TradeOrder();
            order.setMchId(12970L);
            order.setMchNo("No:1001");
            order.setTradeNo(tradeNo);
            order.setBody(productName);
            order.setFee(new BigDecimal(fee));
            order.setChannelId(1L);
            order.setClientIp(clientIp);
            order.setStatus(1); //订单状态：0-订单生成 1-支付中
            order.setCreateTime(new Date());
            redisUtils.set(tradeNo, order);
            return codeUrl;
        } catch (Exception e) {
            logger.error("商户交易号：{} 预支付失败！", tradeNo, e);
        }
        return null;
    }

    /**
     * APP预付款下单
     * @param request
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    public Map<String, String> jsAppOrder(HttpServletRequest request, String productName, String productId, String fee) {
        String nonceStr = sequence.nextNo();
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String tradeNo = attach + sequence.nextValue();
        Map<String,String> paraMap= ParamMap.create("productId", productId)
                .put("body", productName)
                .put("tradeNo", tradeNo)
                .put("fee", fee)
                .put("nonceStr", nonceStr)
                .put("timestamp",timestamp)
                .put("tradeType", "APP")
                .getData();
        try {

            Map<String, String> resultXml = this.prePay(paraMap, SignType.MD5);
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");
            boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode);
            if (!isSuccess) {
                throw new RuntimeException("统一支付失败！" +
                        "return_code:" + resultXml.get("return_code") + " " +
                        "return_msg:" + resultXml.get("return_msg"));
            }
            String prepayId = resultXml.get("prepay_id");
            logger.info("统一下单调用结束！预支付交易标识：{}. {}", prepayId, resultXml.get("return_msg"));

            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("appid", defaultAppId);
            packageMap.put("partnerid", mchId);
            packageMap.put("prepayid", prepayId);
            packageMap.put("package", "Sign=WXPay");
            packageMap.put("noncestr", nonceStr);
            packageMap.put("timestamp", timestamp);
            String sign = SignUtils.createSign(packageMap, paterKey, SignType.MD5);
            packageMap.put("sign", sign);

        } catch (Exception e) {
            logger.error("商户交易号：{} 预支付失败！", tradeNo, e);
        }
        return null;
    }


    /**
     * 微信JSAPI、H5、APP、NATIVE调起支付
     * @param request
     * @param tradeType
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, String> preOrder(HttpServletRequest request, String openId, TradeType tradeType, String productName, String productId, String fee){
        String nonceStr = sequence.nextNo();
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String tradeNo = attach + sequence.nextValue();
        Map<String,String> paraMap= ParamMap.create("openId", openId)
                .put("productId", productId)
                .put("body", productName)
                .put("tradeNo", tradeNo)
                .put("fee", fee)
                .put("nonceStr", nonceStr)
                .put("timestamp",timestamp)
                .put("tradeType", tradeType.toString())
                .getData();
        try {

            Map<String, String> resultXml = this.prePay(paraMap, SignType.MD5);
            //调用统一下单支付结束方法
            String prepayId = parseXml(resultXml);

            Map<String, String> packageMap = new HashMap<>();
            if (TradeType.APP.equals(tradeType)){
                packageMap.put("appid", defaultAppId);
                packageMap.put("partnerid", mchId);
                packageMap.put("prepayid", prepayId);
                packageMap.put("package", "Sign=WXPay");
                packageMap.put("noncestr", nonceStr);
                packageMap.put("timestamp", timestamp);
                String sign = SignUtils.createSign(packageMap, paterKey, SignType.MD5);
                packageMap.put("sign", sign);
            }

            if (TradeType.JSAPI.equals(tradeType)){
                packageMap.put("appid", defaultAppId);
                packageMap.put("timeStamp", timestamp);
                packageMap.put("nonceStr", nonceStr);
                packageMap.put("package", "prepay_id=" + prepayId);
                packageMap.put("signType", "MD5");
                String sign = SignUtils.createSign(packageMap, paterKey, SignType.MD5);
                packageMap.put("paySign", sign);
            }

            if (TradeType.MWEB.equals(tradeType)){
                packageMap.put("webUrl", resultXml.get("mweb_url"));
            }

            if (TradeType.NATIVE.equals(tradeType)){
                packageMap.put("codeUrl", resultXml.get("code_url"));
            }
            return packageMap;
        } catch (Exception e) {
            logger.error("商户交易号：{} 预支付失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }
    }

    /**
     * 预支付接口
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> prePay(Map<String, String> hashMap, SignType signType) throws Exception{
        hashMap.put("appId", defaultAppId);
        hashMap.put("mchId", mchId);
        // 创建请求参数
        Map<String, String> params = createParams(hashMap, signType);
        logger.info("=================统一下单调用开始====================");
        // 微信统一下单
        String result = pushOrder(params);
        Map<String, String> resultXml = BeanUtils.xml2Map(result);
        return resultXml;

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
        params.put("appid", hashMap.get("appId")); //  appId
        params.put("mch_id", hashMap.get("mchId")); //  商户号
        params.put("openid", hashMap.get("openId")); // trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
        params.put("device_info", "WEB"); // 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
        params.put("nonce_str", hashMap.get("nonceStr")); // 随机字符串，不长于32位。
        params.put("body", hashMap.get("body")); // 商品或支付单简要描述
        params.put("attach", attach); // 附加数据=来源
        params.put("out_trade_no", hashMap.get("tradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("fee_type", "CNY"); // 货币类型，默认人民币：CNY
        params.put("total_fee", hashMap.get("fee")); // 订单总金额，单位为分
        params.put("spbill_create_ip", WebUtils.getIP()); // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
        params.put("trade_type", hashMap.get("tradeType")); // JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付、MWEB--H5支付
        params.put("notify_url", payNotify); // 通知地址，接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        params.put("product_id", hashMap.get("productId"));

        String sign = SignUtils.createSign(params, paterKey, signType);
        params.put("sign", sign);

        return params;
    }

    /**
     * 请求下单地址
     * @param params
     * @return
     */
    private static String pushOrder(Map<String, String> params) throws Exception {
        return MchPayAPI.pushOrder(BeanUtils.mapBean2Xml(params));
    }

    /**
     * 解析微信返回xml
     * @param resultXml
     * @return
     */
    private static String parseXml(Map<String, String> resultXml){
        String returnCode = resultXml.get("return_code");
        String resultCode = resultXml.get("result_code");
        boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(resultCode);
        if (!isSuccess) {
            throw new RuntimeException("统一支付失败！" +
                    "return_code:" + resultXml.get("return_code") + " " +
                    "return_msg:" + resultXml.get("return_msg"));
        }
        String prepayId = resultXml.get("prepay_id");
        logger.info("统一下单调用结束！预支付交易标识：{}. {}", prepayId, resultXml.get("return_msg"));
        return prepayId;
    }



    /**
     * 处理微信支付回调
     * @param xmlStr
     * @return
     */
    public String notifyMethod(String xmlStr){
        String successXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        String failXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[${return_msg}]]></return_msg></xml>";
        // 获取参数
        Map<String, String> resultXml = BeanUtils.xml2Map(xmlStr);
        // 获取商户交易号
        String appId = resultXml.get("appid");//获取商户appId
        String mchId = resultXml.get("mch_id");//获取商户号
        String openId = resultXml.get("openid");//获取openId
        String tradeType = resultXml.get("trade_type");//获取交易类型
        String totalFee = resultXml.get("total_fee"); //获取支付金额
        String tradeNo = resultXml.get("out_trade_no");//获取商户交易号
        String transactionId = resultXml.get("transaction_id");//获取微信支付订单号
        String timestamp = resultXml.get("time_end");//获取微信支付完成时间
        try {
            boolean bool = SignUtils.checkSign(resultXml, paterKey, SignType.MD5);
            if (!bool){
                logger.error("微信支付回调验证签名错误！");
                return failXml.replace("${return_msg}", "微信支付回调验证签名错误！");
            }
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");
            boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(resultCode);
            if (isSuccess){
                logger.info("用户公众ID：{} , 订单号：{} , 交易号：{} 微信支付成功！", openId, tradeNo, transactionId);
                //TODO:需要做数据库记录交易订单号
                TradeOrder orders = redisUtils.get(tradeNo);
                //如果支付订单状态不是支付中
                if (orders == null || orders.getStatus() != 1) {
                    throw new RuntimeException("微信支付回调结果未获取到待付款订单!无法修改订单数据,请联系人工处理.");
                }
                orders.setId(sequence.nextValue());
                orders.setStatus(2); //订单状态：0-订单生成 1-支付中 2-支付成功
                orders.setTransactionId(transactionId);
                orders.setSuccessTime(timestamp);
                tradeOrderClient.saveOrder(orders);
                return successXml;
            }
            return failXml;
        } catch (Exception e) {
            logger.error("调用微信支付回调方法异常,商户订单号：{}. 微信支付订单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("调用微信支付回调方法异常！", e);
        }
    }

}
