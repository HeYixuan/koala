package org.igetwell.wechat.app.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.constans.WXPayConstants;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.enums.TradeType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.*;
import org.igetwell.system.bean.dto.request.WxPayRequest;
import org.igetwell.system.bean.dto.request.WxRefundRequest;
import org.igetwell.system.order.feign.TradeOrderClient;
import org.igetwell.system.order.protocol.OrderPayProtocol;
import org.igetwell.system.order.protocol.OrderRefundProtocol;
import org.igetwell.wechat.app.service.IWxPayService;
import org.igetwell.wechat.sdk.api.MchPayAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class WxPayService implements IWxPayService {

    private static final Logger logger = LoggerFactory.getLogger(WxPayService.class);

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

    @Value("${wechat.refundNotify}")
    private String refundNotify;

    @Value("${wechat.cert}")
    private String keyStoreFilePath;

    @Autowired
    private Sequence sequence;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TradeOrderClient tradeOrderClient;


    /**
     * 扫码预付款下单
     */
    public Map<String, String> scan(String tradeNo, String productId, String body, String fee, String clientIp) {
        try {
            String nonceStr = sequence.nextNo();
            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            Map<String,String> paraMap= ParamMap.create("productId", productId)
                    .put("body", body)
                    .put("tradeNo", tradeNo)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .put("timestamp",timestamp)
                    .put("clientIp", clientIp)
                    .put("tradeType", "NATIVE")
                    .getData();

            Map<String, String> resultXml = prePay(paraMap, SignType.MD5);
            String codeUrl = resultXml.get("code_url");
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("codeUrl", codeUrl);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户交易号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }
    }

    /**
     * JSAPI预付款下单
     */
    public Map<String, String> jsapi(String openId, String tradeNo, String body, String fee, String clientIp) {
        try {
            String nonceStr = sequence.nextNo();
            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            Map<String,String> paraMap = ParamMap.create("openId", openId)
                    .put("body", body)
                    .put("tradeNo", tradeNo)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .put("timestamp",timestamp)
                    .put("tradeType", "JSAPI")
                    .getData();
            Map<String, String> resultXml = prePay(paraMap, SignType.MD5);
            String prepayId = resultXml.get("prepay_id");
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("appid", defaultAppId);
            packageMap.put("timeStamp", timestamp);
            packageMap.put("nonceStr", nonceStr);
            packageMap.put("package", "prepay_id=" + prepayId);
            packageMap.put("signType", "MD5");
            String sign = SignUtils.createSign(packageMap, paterKey, SignType.MD5);
            packageMap.put("paySign", sign);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户交易号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }
    }

    /**
     * APP预付款下单
     */
    public Map<String, String> app(String tradeNo, String body, String fee, String clientIp) {
        try {
            String nonceStr = sequence.nextNo();
            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            Map<String,String> paraMap = ParamMap.create("tradeNo", tradeNo)
                    .put("body", body)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .put("timestamp",timestamp)
                    .put("tradeType", "APP")
                    .getData();
            Map<String, String> resultXml = prePay(paraMap, SignType.MD5);
            String prepayId = resultXml.get("prepay_id");
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("appid", defaultAppId);
            packageMap.put("partnerid", mchId);
            packageMap.put("prepayid", prepayId);
            packageMap.put("package", "Sign=WXPay");
            packageMap.put("noncestr", nonceStr);
            packageMap.put("timestamp", timestamp);
            String sign = SignUtils.createSign(packageMap, paterKey, SignType.MD5);
            packageMap.put("sign", sign);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户交易号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }
    }

    /**
     * PC网站预付款下单
     */
    public Map<String, String> web(String tradeNo, String body, String fee, String clientIp) {
        try {
            String nonceStr = sequence.nextNo();
            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            Map<String,String> paraMap = ParamMap.create("tradeNo", tradeNo)
                    .put("body", body)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .put("timestamp",timestamp)
                    .put("tradeType", "MWEB")
                    .getData();
            Map<String, String> resultXml = prePay(paraMap, SignType.MD5);
            String webUrl = resultXml.get("mweb_url");
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("webUrl", webUrl);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户交易号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }
    }

//    private Map<String, String> getExtractMap(Map<String, String> paraMap) throws Exception {
//        Map<String, String> resultXml = this.prePay(paraMap, SignType.MD5);
//        String returnCode = resultXml.get("return_code");
//        String resultCode = resultXml.get("result_code");
//        boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode);
//        if (!isSuccess) {
//            throw new RuntimeException("统一下单失败！" +
//                    "return_code:" + resultXml.get("return_code") + " " +
//                    "return_msg:" + resultXml.get("return_msg"));
//        }
//        logger.info("统一下单调用结束！预支付交易标识：{}. {}", resultXml.get("prepay_id"), resultXml.get("return_msg"));
//
//        return resultXml;
//    }



    /**
     * 微信JSAPI、H5、APP、NATIVE调起支付
     * 注：productId在JSAPI的时候传入openId
     */
    public Map<String, String> preOrder(TradeType tradeType, String tradeNo, String productId, String body, String fee, String clientIp){
        if (TradeType.APP.equals(tradeType)){
            return this.app(tradeNo, body, fee, clientIp);
        }

        if (TradeType.JSAPI.equals(tradeType)){
            return this.jsapi(productId, tradeNo, body, fee, clientIp);
        }

        if (TradeType.MWEB.equals(tradeType)){
            return this.web(tradeNo, body, fee, clientIp);
        } else  {
            //NATIVE扫码支付
            return this.scan(tradeNo, productId, body, fee, clientIp);
        }
    }


    /**
     * 预支付
     * @return
     */
    public Map<String, String> preOrder(WxPayRequest payRequest) {
        if (StringUtils.isEmpty(payRequest)) {
            throw new IllegalArgumentException("交易请求参数不可为空.");
        }
        String tradeNo = payRequest.getTradeNo();
        String productId = payRequest.getProductId();
        String body = payRequest.getBody();
        BigDecimal fee = payRequest.getFee();
        TradeType tradeType = payRequest.getTradeType();
        String clientIp = payRequest.getClientIp();

        if (CharacterUtils.isBlank(tradeType.name())) {
            throw new IllegalArgumentException("交易支付类型不可为空.");
        }
        if (CharacterUtils.isBlank(productId)) {
            throw new IllegalArgumentException("交易支付产品ID不可为空.");
        }
        if (CharacterUtils.isBlank(payRequest.getBody())) {
            throw new IllegalArgumentException("交易支付商品描述不可为空.");
        }
        if (CharacterUtils.isBlank(payRequest.getClientIp())) {
            throw new IllegalArgumentException("客户端IP不可为空.");
        }
        if (BigDecimalUtils.lessThan(fee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("交易金额必须大于0.");
        }
        int amount = BigDecimalUtils.multiply(fee, new BigDecimal(100)).intValue();
        return this.preOrder(tradeType, tradeNo, productId, body, String.valueOf(amount), clientIp);
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
        String result = this.pushOrder(params);
        Map<String, String> resultXml = BeanUtils.xml2Map(result);
        this.parseXml(resultXml);
        return resultXml;
    }

    /**
     * 解析微信返回xml
     * @param resultXml
     * @return
     */
    private static void parseXml(Map<String, String> resultXml){
        String returnCode = resultXml.get("return_code");
        String resultCode = resultXml.get("result_code");
        boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(resultCode);
        if (!isSuccess) {
            StringBuilder builder = new StringBuilder();
            builder.append("统一下单请求失败！return_code:").append(resultXml.get("return_code"));
            builder.append("return_msg:").append(resultXml.get("return_msg"));
            throw new RuntimeException(builder.toString());
        }
        logger.info("统一下单请求结束！预支付交易标识：{}. {}", resultXml.get("prepay_id"), resultXml.get("return_msg"));
    }

    /*private Map<String, String> prePay(Map<String, String> hashMap, SignType signType) throws Exception{
        hashMap.put("appId", defaultAppId);
        hashMap.put("mchId", mchId);
        // 创建请求参数
        Map<String, String> params = createParams(hashMap, signType);
        logger.info("=================统一下单调用开始====================");
        // 微信统一下单
        String result = pushOrder(params);
        Map<String, String> resultXml = BeanUtils.xml2Map(result);
        return resultXml;

    }*/


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
        params.put("product_id", hashMap.get("productId")); // trade_type=NATIVE，此参数必传，此参数为二维码中包含的商品ID，商户自行定义
        params.put("device_info", "WEB"); // 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
        params.put("nonce_str", hashMap.get("nonceStr")); // 随机字符串，不长于32位。
        params.put("body", hashMap.get("body")); // 商品或支付单简要描述
        params.put("attach", attach); // 附加数据=来源
        params.put("out_trade_no", hashMap.get("tradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("fee_type", "CNY"); // 货币类型，默认人民币：CNY
        params.put("total_fee", hashMap.get("fee")); // 订单总金额，单位为分
        params.put("spbill_create_ip", hashMap.get("clientIp")); // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
        params.put("trade_type", hashMap.get("tradeType")); // JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付、MWEB--H5支付
        params.put("notify_url", payNotify); // 通知地址，接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

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
     * 处理微信支付回调
     * @param xmlStr
     * @return
     */
    public String payNotify(String xmlStr){
        logger.info("[微信支付]-进入微信支付回调开始.");
        String successXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        String failXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[${return_msg}]]></return_msg></xml>";
        // 获取参数
        Map<String, String> resultXml = BeanUtils.xml2Map(xmlStr);
        // 获取商户交易号
        String appId = resultXml.get("appid");//获取商户appId
        String mchId = resultXml.get("mch_id");//获取商户号
        String openId = resultXml.get("openid");//获取openId
        String fee = resultXml.get("total_fee"); //获取支付金额
        String tradeNo = resultXml.get("out_trade_no");//获取商户交易号
        String transactionId = resultXml.get("transaction_id");//获取微信支付订单号
        String timestamp = resultXml.get("time_end");//获取微信支付完成时间
        try {
            boolean bool = SignUtils.checkSign(resultXml, paterKey, SignType.MD5);
            if (!bool){
                logger.error("[微信支付]-微信支付回调验证签名错误！");
                return failXml.replace("${return_msg}", "微信支付回调验证签名错误!");
            }
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");
            boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(resultCode);
            if (!isSuccess){
                logger.info("[微信支付]-用户公众ID：{} , 订单号：{} , 交易号：{} 微信支付失败!", openId, tradeNo, transactionId);
                return failXml;
            }
            BigDecimal totalFee = BigDecimalUtils.divide(new BigDecimal(fee), new BigDecimal(100));
            OrderPayProtocol protocol = new OrderPayProtocol(tradeNo, transactionId, totalFee, timestamp);
            rocketMQTemplate.getProducer().setProducerGroup("trade-order-success");
            rocketMQTemplate.asyncSend("trade-order-success:trade-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    logger.info("[微信支付]-异步投递支付成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                    logger.info("[微信支付]-异步投递支付成功订单消息成功,投递结果：{}. ", var);
                }
                @Override
                public void onException(Throwable var) {
                    logger.error("[微信支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
                }
            });

            logger.info("[微信支付]-用户公众ID：{} , 商户订单号：{} , 微信交易号：{} 微信支付成功!", openId, tradeNo, transactionId);
            return successXml;
        } catch (Exception e) {
            logger.error("[微信支付]-调用微信支付回调方法异常,商户订单号：{}, 微信交易号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("调用微信支付回调方法异常！", e);
        }
    }

    /**
     * 微信退款
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @throws Exception
     */
    public void refund(String transactionId, String tradeNo, String outNo, String totalFee, String fee) {
        try{
            logger.info("[微信支付]-发起微信退款请求开始. 微信支付单号:{}, 商户单号：{}, 退款金额：{}.", transactionId, tradeNo, fee);
            if (CharacterUtils.isBlank(transactionId) || CharacterUtils.isBlank(tradeNo) || CharacterUtils.isBlank(fee)) {
                logger.error("[微信支付]-发起微信退款失败. 请求参数为空. 微信支付单号:{}, 商户单号：{}, 退款金额：{}.", transactionId, tradeNo, fee);
                throw new IllegalArgumentException("[微信支付]-发起微信退款失败. 请求参数为空.");
            }
            String nonceStr = sequence.nextNo();
            Map<String,String> paraMap= ParamMap.create("transactionId", transactionId)//微信订单号
                    .put("tradeNo", tradeNo)//订单号
                    .put("outNo", outNo) //退款单号
                    .put("totalFee", totalFee)
                    .put("fee", fee)
                    .put("nonceStr", nonceStr)
                    .getData();
            // 创建请求参数
            Map<String, String> params = createRefundParams(paraMap, SignType.MD5);
            logger.info("[微信支付]-微信退款调用开始. 请求微信退款参数：{}.", GsonUtils.toJson(params));
            //微信退款
            String result = this.refundOrder(params);
            Map<String, String> resultXml = BeanUtils.xml2Map(result);
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");

            boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode);
            if (!isSuccess) {
                String returnMsg = resultXml.get("err_code_des");
                if (!("订单已全额退款").equals(returnMsg)) {
                    logger.error("[微信支付]-微信退款失败!  商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
                    throw new RuntimeException("[微信支付]-微信退款失败." + resultXml.get("return_msg"));
                }
            }
            logger.info("[微信支付]-微信退款成功! 商户退款单号：{}, 商户订单号：{}, 微信支付订单号：{}.", outNo, tradeNo, transactionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 微信退款
     */
    public void refund(WxRefundRequest refundRequest){
        if (StringUtils.isEmpty(refundRequest)) {
            throw new IllegalArgumentException("退款请求参数不可为空.");
        }
        String tradeNo = refundRequest.getTradeNo();
        String transactionId = refundRequest.getTransactionId();
        String outNo = refundRequest.getOutNo();
        BigDecimal totalFee = refundRequest.getTotalFee();
        BigDecimal fee = refundRequest.getFee();
        if (StringUtils.isEmpty(refundRequest)) {
            throw new IllegalArgumentException("退款请求参数不可为空.");
        }
        if (StringUtils.isEmpty(outNo)) {
            throw new IllegalArgumentException("商户退款单号不可为空.");
        }
        if (CharacterUtils.isBlank(tradeNo)) {
            throw new IllegalArgumentException("商户交易单号不可为空.");
        }
        if (CharacterUtils.isBlank(transactionId)) {
            throw new IllegalArgumentException("商户交易流水单号不能为空.");
        }
        if (StringUtils.isEmpty(totalFee) || BigDecimalUtils.lessThan(totalFee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("退款订单总额必须大于0.");
        }
        if (StringUtils.isEmpty(fee) || BigDecimalUtils.lessThan(fee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("退款金额额必须大于0.");
        }
        if (BigDecimalUtils.lessThan(totalFee, fee)) {
            throw new IllegalArgumentException("退款金额不能大于订单总额.");
        }

        int totalMoney = BigDecimalUtils.multiply(totalFee, new BigDecimal(100)).intValue();
        int money = BigDecimalUtils.multiply(fee, new BigDecimal(100)).intValue();
        this.refund(transactionId, tradeNo, outNo, String.valueOf(totalMoney), String.valueOf(money));

    }

    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createRefundParams(Map<String, String> hashMap, SignType signType) throws Exception {
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
     * 申请退款
     */
    private String refundOrder(Map<String, String> params) throws Exception {
        return MchPayAPI.refund(mchId, keyStoreFilePath, BeanUtils.mapBean2Xml(params));
    }


    /**
     * 处理微信退款回调
     * @param xmlStr
     * @return
     */
    public String refundNotify(String xmlStr) {
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
        String refundAccount = subXml.get("refund_recv_accout");//退回账户
        String status = subXml.get("refund_status");//退款状态：SUCCESS-退款成功CHANGE-退款异常REFUNDCLOSE—退款关闭
        String timestamp = subXml.get("success_time");//退款成功时间

        try {
            String returnCode = resultXml.get("return_code");
            boolean isSuccess = WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode) && WXPayConstants.SUCCESS.equalsIgnoreCase(status);
            if (!isSuccess){
                logger.error("[微信支付]-微信退款失败! 商户订单号：{}, 微信支付订单号：{}, 商户退款号：{}." , tradeNo, transactionId, outNo, refundId, timestamp);
                throw new RuntimeException("微信退款失败!");
            }
            OrderRefundProtocol protocol = new OrderRefundProtocol(outNo, transactionId, tradeNo, refundId, refundAccount, timestamp);
            rocketMQTemplate.getProducer().setProducerGroup("refund-order-success");
            rocketMQTemplate.asyncSend("refund-order-success:refund-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    logger.info("[微信支付]-异步投递退款成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                    logger.info("[微信支付]-异步投递退款成功订单消息成功,投递结果：{}. ", var);
                }
                @Override
                public void onException(Throwable var) {
                    logger.error("[微信支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
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
     * 查询支付订单
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    public ResponseEntity getOrder(String transactionId, String tradeNo) {
        try{
            logger.info("[微信支付]-发起微信查询支付订单请求开始. 微信支付单号:{}, 商户单号：{}.", transactionId, tradeNo);
            if (CharacterUtils.isBlank(transactionId) || CharacterUtils.isBlank(tradeNo)) {
                logger.error("[微信支付]-发起微信查询支付订单请求失败. 请求参数为空. 微信支付单号:{}, 商户单号：{}.", transactionId, tradeNo);
                throw new IllegalArgumentException("[微信支付]-发起微信查询支付订单请求失败. 请求参数为空.");
            }
            String nonceStr = sequence.nextNo();
            Map<String,String> paraMap= ParamMap.create("transactionId", transactionId)//微信订单号
                    .put("tradeNo", tradeNo)//商户订单号
                    .put("nonceStr", nonceStr)
                    .getData();
            // 创建请求参数
            Map<String, String> params = this.createQueryParams(paraMap, SignType.MD5);
            logger.info("[微信支付]-微信查询支付订单调用开始. 请求微信查询支付订单参数：{}.", GsonUtils.toJson(params));
            //微信查询支付订单
            String result = this.queryOrder(params);
            Map<String, String> resultXml = BeanUtils.xml2Map(result);
            String returnCode = resultXml.get("return_code");
            String resultCode = resultXml.get("result_code");
            String tradeState = resultXml.get("trade_state");

            boolean isSuccess = WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode) && WXPayConstants.SUCCESS.equals(tradeState);
            if (!isSuccess) {
                logger.error("[微信支付]-查询支付订单成功,此交易订单未支付! 商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
                return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此交易订单未支付!");
            }
            logger.info("[微信支付]-查询支付订单成功,此交易已支付! 商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
            return ResponseEntity.ok();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createQueryParams(Map<String, String> hashMap, SignType signType) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("appid", defaultAppId);//appId
        params.put("mch_id", mchId);//商户号
        params.put("transaction_id", hashMap.get("transactionId"));//微信交易号
        params.put("out_trade_no", hashMap.get("tradeNo"));//商户订单号
        params.put("nonce_str", hashMap.get("nonceStr"));//随机字符串，不长于32位。
        String sign = SignUtils.createSign(params, paterKey, signType);
        params.put("sign", sign);
        return params;
    }

    /**
     * 查询支付订单
     */
    private String queryOrder(Map<String, String> params) throws Exception {
        return MchPayAPI.queryOrder(BeanUtils.mapBean2Xml(params));
    }
}
