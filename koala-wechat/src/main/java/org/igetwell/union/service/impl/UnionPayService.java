package org.igetwell.union.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.constans.PayConstants;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.uitls.*;
import org.igetwell.system.order.protocol.OrderPayProtocol;
import org.igetwell.system.order.protocol.OrderRefundProtocol;
import org.igetwell.union.service.IUnionPayService;
import org.igetwell.union.sdk.UnionSignUtils;
import org.igetwell.wechat.sdk.api.MchPayAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UnionPayService implements IUnionPayService {

    private static final Logger logger = LoggerFactory.getLogger(UnionPayService.class);

    @Value("${union.appId}")
    private String defaultAppId;
    @Value("${union.payNotify}")
    private String payNotify;
    @Value("${union.refundNotify}")
    private String refundNotify;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 手机网站支付预付款下单
     */
    public Map<String, String> wap(String tradeNo, String subject, String body, String fee) {
        Map<String, String> paraMap = ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 07：申请消费二维码
                .put("bizType", "000201") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
        String html = this.createAutoFormHtml(frontTransUrl, params, "UTF-8");   //生成自动跳转的Html表单
        paraMap.put("page", html);
        return paraMap;
    }

    /**
     * PC网站支付
     */
    public Map<String, String> web(String tradeNo, String subject, String body, String fee) {
        Map<String, String> paraMap = ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 07：申请消费二维码
                .put("bizType", "000201") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "07") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
        String html = this.createAutoFormHtml(frontTransUrl, params, "UTF-8");   //生成自动跳转的Html表单
        paraMap.put("page", html);
        return paraMap;
    }


    /**
     * 商户统一下单(用户主扫)
     */
    public Map<String, String> unifiedOrder(String tradeNo, String subject, String body, String fee) {
        Map<String, String> paraMap = ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 01：统一下单
                .put("bizType", "000000") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/order.do";
        Map<String, String> resultMap = this.pushOrder(frontTransUrl, params);
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))) {
            throw new RuntimeException("[银联支付]-银联支付失败");
        }
        /*String codeUrl = URLDecoder.decode(resultMap.get("payUrl", "UTF-8");
        String userAgent = WebUtils.getBrowserName();
        if(CharacterUtils.isNotBlank(userAgent)){
            final String appUaReg = ".*(UnionPay/[^ ]+ ([0-9a-zA-Z]+)).*"; //各类云闪付app跳转payUrl会跳去二维码支付页面
            final String appleUaReg = ".*((iPhone|iPad).*Safari).*"; //applepay跳转payUrl会调起ApplePay支付，这里随便写了个正则，请根据实际情况修改
            if(Pattern.compile(appUaReg).matcher(ua).matches()){
                resp.sendRedirect(payUrl);
                return;
            }
            if(Pattern.compile(appleUaReg).matcher(ua).matches()){
                resp.sendRedirect(payUrl);
                return;
            }
        }*/
        String codeUrl = resultMap.get("payUrl");
        Map<String, String> packageMap = new HashMap<>();
        packageMap.put("codeUrl", codeUrl);

        return packageMap;
    }

    /**
     * 扫码预付款下单(用户主扫)
     */
    public Map<String, String> scan(String tradeNo, String subject, String body, String fee) {
        Map<String, String> paraMap = ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "07") //交易子类 07：申请消费二维码
                .put("bizType", "000000") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
        Map<String, String> resultMap = this.pushOrder(frontTransUrl, params);
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))) {
            throw new RuntimeException("[银联支付]-银联支付失败");
        }
        String codeUrl = resultMap.get("qrCode");
        Map<String, String> packageMap = new HashMap<>();
        packageMap.put("codeUrl", codeUrl);
        return packageMap;
    }

    /**
     * 签名入参
     *
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createParams(Map<String, String> hashMap, SignType signType) {
        Map<String, String> params = new HashMap<>();
        params.put("merId", defaultAppId); //  商户号
        params.put("orderId", hashMap.get("tradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("version", "5.1.0"); //全渠道版本号
        params.put("encoding", "UTF-8"); //全渠道版本号
        params.put("channelType", hashMap.get("channelType")); //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
        params.put("accessType", "0"); // 接入类型0：商户直连接入 1：收单机构接入 2：平台商户接入
        params.put("txnType", "01"); //交易类型 ，01：消费
        params.put("txnSubType", hashMap.get("txnSubType")); //01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付） 03：分期付款 07：申请消费二维码
        params.put("txnAmt", hashMap.get("fee")); // 交易金额，单位为分
        params.put("currencyCode", "156"); // 货币类型，默认人民币：CNY
        params.put("txnTime", DateUtils.formaDateTime(LocalDateTime.now())); //商户发送交易时间
        params.put("bizType", hashMap.get("bizType")); //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
        params.put("backUrl", payNotify); // 通知地址
        params.put("frontUrl", "http://localhost:8080/ACPSample_B2C/frontRcvResponse");
        params.put("signMethod", "01"); //非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
        params.put("payTimeout", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime() + 30 * 60 * 1000));
        params.put("tradeType", "mobileWeb");                    //交易场景 固定填写	mobileWeb
        String sign = UnionSignUtils.createSign(params);
        params.put("signature", sign); //设置签名域值
        logger.info("=================统一下单调用开始====================");
        return params;
    }

    private static Map<String,String> pushOrder(String url, Map<String, String> params) {
        try {
            String result = MchPayAPI.execute(url, params);
            return ParamMap.getUrlMap(result);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 银联支付回调
     *
     * @return
     */
    public String payNotify(HttpServletRequest request) {
        logger.info("[银联支付]-银联支付发起支付回调请求开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);
        String transactionId = params.get("queryId");//银联交易流水号
        String tradeNo = params.get("orderId"); //商户订单号
        String timestamp = params.get("txnTime");
        String fee = params.get("txnAmt");//支付金额
        try {
            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (params.isEmpty() || !UnionSignUtils.checkSign(params) || !"00".equals(params.get("respCode"))) {
                logger.error("[银联支付]-银联支付回调验证签名错误！");
                return PayConstants.ERROR;
            }
            BigDecimal totalFee = BigDecimalUtils.divide(new BigDecimal(fee), new BigDecimal(100));
            OrderPayProtocol protocol = new OrderPayProtocol(tradeNo, transactionId, totalFee, timestamp);
            rocketMQTemplate.getProducer().setProducerGroup("trade-order-success");
            rocketMQTemplate.asyncSend("trade-order-success:trade-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    logger.info("[银联支付]-异步投递支付成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                    logger.info("[银联支付]-异步投递支付成功订单消息成功,投递结果：{}. ", var);
                }
                @Override
                public void onException(Throwable var) {
                    logger.error("[银联支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
                }
            });
            logger.info("[银联支付]-商户订单号：{}, 银联交易单号：{} 银联支付成功!", tradeNo, transactionId);
            return PayConstants.OK;
        } catch (Exception e){
            logger.error("[银联支付]-调用银联支付回调方法异常,商户订单号：{}, 银联交易单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("调用银联支付回调方法异常！", e);
        }
    }

    /**
     * 银联支付回调
     * @return
     */
    public String refundNotify(HttpServletRequest request){
        logger.info("[银联支付]-银联发起退款回调请求开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);
        String transactionId = params.get("origQryId"); //原始消费交易银联交易流水号
        String outTradeNo = params.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
        String tradeNo = params.get("reqReserved"); //原订单号
        String refundId = params.get("queryId");//银联退款交易流水号
        String timestamp = params.get("txnTime");
        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!UnionSignUtils.checkSign(params)) {
            logger.error("[银联支付]-银联退款回调验证签名错误！");
            return PayConstants.ERROR;
        }
        if (params.isEmpty() || !"00".equals(params.get("respCode"))) {
            throw new RuntimeException("[银联支付]-银联回调退款失败.");
        }
        OrderRefundProtocol protocol = new OrderRefundProtocol(outTradeNo, transactionId, tradeNo, refundId, "银联", timestamp);
        rocketMQTemplate.getProducer().setProducerGroup("refund-order-success");
        rocketMQTemplate.asyncSend("refund-order-success:refund-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult var) {
                logger.info("[银联支付]-异步投递退款成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                logger.info("[银联支付]-异步投递退款成功订单消息成功,投递结果：{}. ", var);
            }
            @Override
            public void onException(Throwable var) {
                logger.error("[微信支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
            }
        });
        logger.info("[银联支付]-银联退款成功! 商户订单号：{}, 银联支付订单号：{}, 商户退款单号：{}, 银联退款单号：{}, 退款成功时间: {}." , tradeNo, transactionId, outTradeNo, refundId, timestamp);
        return PayConstants.OK;
    }


    public void refund(String transactionId, String tradeNo, String outTradeNo, String fee){
        logger.info("[银联支付]-发起银联退款请求开始. 银联支付单号:{}, 商户订单号, 退款金额：{}.", transactionId, tradeNo, fee);
        Map<String, String> paraMap = ParamMap.create("transactionId", transactionId) //银联支付交易流水号
                .put("tradeNo", tradeNo)//订单号
                .put("outTradeNo", outTradeNo) //退款单号
                .put("fee", fee)
                .put("txnSubType", "00")  //交易子类型  默认00
                .put("bizType", "000201") //业务类型 B2C网关支付，手机wap支付
                .put("channelType", "07") //渠道类型，07-PC，08-手机
                .getData();
        Map<String, String> params = createRefundParams(paraMap, null);
        logger.info("[银联支付]-银联退款调用开始. 请求银联退款参数：{}.", GsonUtils.toJson(params));
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
        Map<String, String> resultMap = this.pushOrder(frontTransUrl, params);
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))) {
            throw new RuntimeException("[银联支付]-银联退款失败.");
        }
        String queryId = resultMap.get("queryId");
        logger.info("[银联支付]-银联退款成功! 商户退款单号：{}, 商户订单号：{}, 银联支付单号：{}, 银联退款单号：{}.", outTradeNo, tradeNo, transactionId, queryId);
    }

    /**
     * 查询支付订单
     * @param transactionId 银联订单号
     * @param tradeNo 商户订单号
     * @return
     */
    public ResponseEntity getOrder(String transactionId, String tradeNo){
        Map<String, String> paraMap = ParamMap.create("transactionId", transactionId) //银联支付交易流水号
                .put("tradeNo", tradeNo)//订单号
                .put("txnSubType", "00")  //交易子类型  默认00
                .put("bizType", "000201") //业务类型 B2C网关支付，手机wap支付
                .getData();
        Map<String, String> params = this.createQueryParams(paraMap, null);
        logger.info("[银联支付]-银联查询支付订单调用开始. 请求银联查询支付订单参数：{}.", GsonUtils.toJson(params));
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/queryTrans.do";
        Map<String, String> resultMap = this.pushOrder(frontTransUrl, params);
        if (resultMap.isEmpty()) {
            throw new RuntimeException("查询支付订单失败!!");
        }
        if ("00".equals(resultMap.get("respCode"))) {
            String origRespCode = resultMap.get("origRespCode");
            if ("00".equals(origRespCode)){
                logger.info("[银联支付]-查询支付订单成功,此交易已支付! 商户订单号：{}, 银联支付订单号：{}.", tradeNo, transactionId);
                return ResponseEntity.ok();
            }
            if("05".equals(origRespCode) || "39".equals(origRespCode)){
                logger.error("[银联支付]-查询支付订单成功,此交易订单未支付! 商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
                return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此交易订单未支付!");

            }
             else{
                throw new RuntimeException("查询支付订单失败!!");
            }
        } if("34".equals(resultMap.get("respCode"))){
            logger.error("[银联支付]-查询支付订单成功,无此交易订单或已退款! 商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "无此交易订单或已退款!");
        } else {
            logger.error("[银联支付]-查询支付订单失败,此交易订单未支付! 商户订单号：{}, 微信支付订单号：{}.", tradeNo, transactionId);
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "查询支付订单失败!");
        }
    }

    /**
     * 关闭订单
     * @param outTradeNo 关闭订单号
     * @param tradeNo 商户订单号
     * @return
     */
    public ResponseEntity closeOrder(String outTradeNo, String transactionId, String tradeNo, String fee){
        logger.info("[银联支付]-发起银联关闭订单请求开始. 商户单号：{}.", tradeNo);
        Map<String, String> paraMap = ParamMap.create("tradeNo", tradeNo)
                .put("transactionId", transactionId) //银联支付交易流水号
                .put("tradeNo", tradeNo)//订单号
                .put("outTradeNo", outTradeNo) //关闭单号
                .put("fee", fee)
                .put("txnSubType", "00")  //交易子类型  默认00
                .put("bizType", "000201") //业务类型 B2C网关支付，手机wap支付
                .put("channelType", "07") //渠道类型，07-PC，08-手机
                .getData();
        Map<String, String> params = this.createCloseParams(paraMap, null);
        logger.info("[银联支付]-银联关闭订单调用开始. 请求银联关闭订单参数：{}.", GsonUtils.toJson(params));
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
        Map<String, String> resultMap = this.pushOrder(frontTransUrl, params);
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))) {
            logger.error("[银联支付]-关闭订单失败! 商户订单号：{}.", tradeNo);
            throw new RuntimeException("关闭订单失败.");
        }
        logger.info("[微信支付]-关闭订单成功! 商户订单号：{}.", tradeNo);
        return ResponseEntity.ok();
    }

    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createRefundParams(Map<String, String> hashMap, SignType signType) {
        Map<String, String> params = new HashMap<>();
        params.put("merId", defaultAppId); //  商户号
        params.put("orderId", hashMap.get("outTradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("origQryId", hashMap.get("transactionId"));      //银联支付交易流水号
        params.put("origOrderId", hashMap.get("tradeNo"));      //原订单号
        params.put("version", "5.1.0"); //全渠道版本号
        params.put("encoding", "UTF-8"); //全渠道版本号
        params.put("channelType", hashMap.get("channelType")); //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
        params.put("accessType", "0"); // 接入类型0：商户直连接入 1：收单机构接入 2：平台商户接入
        params.put("txnType", "04"); //交易类型 ，01：消费 04:退款
        params.put("txnSubType", hashMap.get("txnSubType")); //01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付） 03：分期付款 07：申请消费二维码
        params.put("txnAmt", hashMap.get("fee")); // 交易金额，单位为分
        params.put("currencyCode", "156"); // 货币类型，默认人民币：CNY
        params.put("txnTime", DateUtils.formaDateTime(LocalDateTime.now())); //商户发送交易时间
        params.put("bizType", hashMap.get("bizType")); //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
        params.put("backUrl", refundNotify); // 通知地址
        params.put("signMethod", "01"); //非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
        params.put("reqReserved", hashMap.get("tradeNo")); //原订单号
        String sign = UnionSignUtils.createSign(params);
        params.put("signature", sign); //设置签名域值
        return params;
    }


    /**
     * 功能：前台交易构造HTTP POST自动提交表单<br>
     * @param reqUrl 表单提交地址<br>
     * @param hiddens 以MAP形式存储的表单键值<br>
     * @param encoding 上送请求报文域encoding字段的值<br>
     * @return 构造好的HTTP POST交易表单<br>
     */
    private static String createAutoFormHtml(String reqUrl, Map<String, String> hiddens,String encoding) {
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + reqUrl
                + "\" method=\"post\">");
        if (null != hiddens && 0 != hiddens.size()) {
            Set<Map.Entry<String, String>> set = hiddens.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> ey = it.next();
                String key = ey.getKey();
                String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
                        + key + "\" value=\"" + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }



    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createQueryParams(Map<String, String> hashMap, SignType signType) {
        Map<String, String> params = new HashMap<>();
        params.put("merId", defaultAppId); //  商户号
        params.put("orderId", hashMap.get("tradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("version", "5.1.0"); //全渠道版本号
        params.put("encoding", "UTF-8"); //全渠道版本号
        params.put("accessType", "0"); // 接入类型0：商户直连接入 1：收单机构接入 2：平台商户接入
        params.put("txnType", "00"); //交易类型 ，01：消费 04:退款 00:查询交易
        params.put("txnSubType", hashMap.get("txnSubType")); //01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付） 03：分期付款 07：申请消费二维码
        params.put("txnTime", DateUtils.formaDateTime(LocalDateTime.now())); //商户发送交易时间
        params.put("bizType", hashMap.get("bizType")); //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
        params.put("signMethod", "01"); //非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
        params.put("reqReserved", hashMap.get("transactionId")); //银联交易流水号
        String sign = UnionSignUtils.createSign(params);
        params.put("signature", sign); //设置签名域值
        return params;
    }

    /**
     * 签名入参
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> createCloseParams(Map<String, String> hashMap, SignType signType) {
        Map<String, String> params = new HashMap<>();
        params.put("merId", defaultAppId); //  商户号
        params.put("orderId", hashMap.get("outTradeNo")); // 商户系统内部的订单号,32个字符内、可包含字母
        params.put("origQryId", hashMap.get("transactionId"));      //银联支付交易流水号
        params.put("origOrderId", hashMap.get("tradeNo"));      //原订单号
        params.put("version", "5.1.0"); //全渠道版本号
        params.put("encoding", "UTF-8"); //全渠道版本号
        params.put("channelType", hashMap.get("channelType")); //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
        params.put("accessType", "0"); // 接入类型0：商户直连接入 1：收单机构接入 2：平台商户接入
        params.put("txnType", "31"); //交易类型 ，01：消费 04:退款 31:消费撤销
        params.put("txnSubType", hashMap.get("txnSubType")); //01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付） 03：分期付款 07：申请消费二维码
        params.put("txnAmt", hashMap.get("fee")); // 交易金额，单位为分
        params.put("txnTime", DateUtils.formaDateTime(LocalDateTime.now())); //商户发送交易时间
        params.put("bizType", hashMap.get("bizType")); //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
        params.put("backUrl", refundNotify); // 通知地址
        params.put("signMethod", "01"); //非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
        params.put("reqReserved", hashMap.get("tradeNo")); //原订单号
        String sign = UnionSignUtils.createSign(params);
        params.put("signature", sign); //设置签名域值
        return params;
    }


}
