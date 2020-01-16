package org.igetwell.alipay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.alipay.service.IAlipayService;
import org.igetwell.common.constans.PayConstants;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.enums.TradeType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.*;
import org.igetwell.system.bean.dto.request.AliPayRequest;
import org.igetwell.system.bean.dto.request.AliRefundRequest;
import org.igetwell.system.order.protocol.OrderPayProtocol;
import org.igetwell.system.order.protocol.OrderRefundProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlipayService implements IAlipayService {

    private static final Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Value("${alipay.appId}")
    private String appId;
    @Value("${attach}")
    private String attach;
    @Value("${alipay.gateway}")
    private String gateway;
    @Value("${alipay.payNotify}")
    private String payNotify;
    @Value("${alipay.returnNotify}")
    private String returnNotify;
    @Value("${alipay.refundNotify}")
    private String refundNotify;

    @Value("${alipay.public-key}")
    private String publicKey;
    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;
    @Value("${alipay.private-key}")
    private String privateKey;

    @Autowired
    private Sequence sequence;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 扫码预付款下单
     */
    @Override
    public Map<String, String> wap(String tradeNo, String subject, String body, String fee) {
        try {
            //创建交易信息模型对象
            AlipayTradeWapPayModel precreateModel = new AlipayTradeWapPayModel();
            //商户订单号，需要保证不重复
            precreateModel.setOutTradeNo(tradeNo);
            //订单金额
            precreateModel.setTotalAmount(fee);
            //交易主题
            precreateModel.setSubject(subject);
            //商品名称
            precreateModel.setBody(body);
            //销售产品码，商家和支付宝签约的产品码，该产品请填写固定值：QUICK_WAP_WAY
            precreateModel.setProductCode("QUICK_WAP_PAY");
            //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
            precreateModel.setTimeoutExpress("30m");
            //创建阿里请求对象
            AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
            //业务请求参数的集合
            alipayTradeWapPayRequest.setBizModel(precreateModel);
            //设置后台异步通知的地址，在手机端支付后支付宝会通知后台(成功或者失败)，手机端的真实支付结果依赖于此地址
            alipayTradeWapPayRequest.setNotifyUrl(payNotify);
            //支付成功后的跳转页面,由于前台回跳的不可靠性，前台回跳只能作为商户支付结果页的入口，最终支付结果必须以异步通知或查询接口返回为准，不能依赖前台回跳
            alipayTradeWapPayRequest.setReturnUrl(returnNotify);
            //创建阿里客户端对象
            AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey, "json", "UTF-8", alipayPublicKey, SignType.RSA2.name());
            String pageForm = alipayClient.pageExecute(alipayTradeWapPayRequest).getBody();
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("pageForm", pageForm);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户订单号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }

    }


    /**
     * PC网站支付
     */
    @Override
    public Map<String, String> web(String tradeNo, String subject, String body, String fee) {
        try {
            //创建交易信息模型对象
            AlipayTradePagePayModel precreateModel = new AlipayTradePagePayModel();
            //商户订单号，需要保证不重复
            precreateModel.setOutTradeNo(tradeNo);
            //订单金额
            precreateModel.setTotalAmount(fee);
            //交易主题
            precreateModel.setSubject(subject);
            //商品名称
            precreateModel.setBody(body);
            //销售产品码，商家和支付宝签约的产品码，该产品请填写固定值：QUICK_WAP_WAY
            precreateModel.setProductCode("FAST_INSTANT_TRADE_PAY");
            //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
            precreateModel.setTimeoutExpress("30m");
            //创建阿里请求对象
            AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
            //业务请求参数的集合
            alipayTradePagePayRequest.setBizModel(precreateModel);
            //设置后台异步通知的地址，在手机端支付后支付宝会通知后台(成功或者失败)，手机端的真实支付结果依赖于此地址
            alipayTradePagePayRequest.setNotifyUrl(payNotify);
            //支付成功后的跳转页面,由于前台回跳的不可靠性，前台回跳只能作为商户支付结果页的入口，最终支付结果必须以异步通知或查询接口返回为准，不能依赖前台回跳
            alipayTradePagePayRequest.setReturnUrl(returnNotify);
            //创建阿里客户端对象
            AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey, "json", "UTF-8", alipayPublicKey, SignType.RSA2.name());
            String pageForm = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("pageForm", pageForm);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户订单号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }

    }

    /**
     * 扫码预付款下单
     */
    @Override
    public Map<String, String> scan(String tradeNo, String subject, String body, String fee) {
        try {
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();//创建API对应的request类
            //商户订单号，需要保证不重复
            model.setOutTradeNo(tradeNo);
            //订单金额
            model.setTotalAmount(fee);
            //交易主题
            model.setSubject(subject);
            //商品名称
            model.setBody(body);
            //销售产品码，不传默认使用FACE_TO_FACE_PAYMENT
            model.setProductCode("FACE_TO_FACE_PAYMENT");
            //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
            model.setTimeoutExpress("30m");
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
            request.setBizModel(model);
            //设置后台异步通知的地址，在手机端支付后支付宝会通知后台(成功或者失败)，手机端的真实支付结果依赖于此地址
            request.setNotifyUrl(payNotify);
            //支付成功后的跳转页面,由于前台回跳的不可靠性，前台回跳只能作为商户支付结果页的入口，最终支付结果必须以异步通知或查询接口返回为准，不能依赖前台回跳
            request.setReturnUrl(returnNotify);
            AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey, "json", "UTF-8", alipayPublicKey, SignType.RSA2.name());
            String codeUrl = alipayClient.execute(request).getQrCode();
            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("codeUrl", codeUrl);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户订单号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }

    }


    /**
     * 创建订单 JSAPI 通过支付宝交易号唤起收银台支付
     * https://docs.open.alipay.com/common/105591
     */
    @Override
    public Map<String, String> jsapi(String tradeNo, String subject, String body, String fee) {
        try {
            AlipayTradeCreateModel model = new AlipayTradeCreateModel();//创建API对应的request类
            //商户订单号，需要保证不重复
            model.setOutTradeNo(tradeNo);
            //订单金额
            model.setTotalAmount(fee);
            //交易主题
            model.setSubject(subject);
            //商品名称
            model.setBody(body);
            //销售产品码，不传默认使用FACE_TO_FACE_PAYMENT
            model.setProductCode("FACE_TO_FACE_PAYMENT");
            //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
            model.setTimeoutExpress("30m");
            AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();//创建API对应的request类
            request.setBizModel(model);
            //设置后台异步通知的地址，在手机端支付后支付宝会通知后台(成功或者失败)，手机端的真实支付结果依赖于此地址
            request.setNotifyUrl(payNotify);
            //支付成功后的跳转页面,由于前台回跳的不可靠性，前台回跳只能作为商户支付结果页的入口，最终支付结果必须以异步通知或查询接口返回为准，不能依赖前台回跳
            request.setReturnUrl(returnNotify);
            AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey, "json", "UTF-8", alipayPublicKey, SignType.RSA2.name());
            String transactionId = alipayClient.execute(request).getTradeNo();

            Map<String, String> packageMap = new HashMap<>();
            packageMap.put("transactionId", transactionId);
            return packageMap;
        } catch (Exception e) {
            logger.error("商户订单号：{} 创建预支付订单失败！", tradeNo, e);
            throw new RuntimeException("创建预支付订单失败！", e);
        }

    }

    /**
     * 预下单
     *
     * @return
     */
    public Map<String, String> preOrder(TradeType tradeType, String tradeNo, String subject, String body, String fee) {
        if (TradeType.WAP.equals(tradeType)) {
            return this.wap(tradeNo, subject, body, fee);
        }
        if (TradeType.PC.equals(tradeType)) {
            return this.web(tradeNo, subject, body, fee);
        }
        if (TradeType.JSAPI.equals(tradeType)){
            return this.jsapi(tradeNo, subject, body, fee);
        } else {
            //FACE 当面扫
            return this.scan(tradeNo, subject, body, fee);
        }
    }

    /**
     * 预下单
     *
     * @param payRequest
     * @return
     */
    public Map<String, String> preOrder(AliPayRequest payRequest) {
        String tradeNo = payRequest.getTradeNo();
        String productId = payRequest.getProductId();
        String body = payRequest.getBody();
        BigDecimal fee = payRequest.getFee();
        TradeType tradeType = payRequest.getTradeType();
        if (CharacterUtils.isBlank(tradeType.name())) {
            throw new IllegalArgumentException("交易支付类型不可为空.");
        }
        if (CharacterUtils.isBlank(productId)) {
            throw new IllegalArgumentException("交易支付产品ID不可为空.");
        }
        if (CharacterUtils.isBlank(payRequest.getBody())) {
            throw new IllegalArgumentException("交易支付商品描述不可为空.");
        }
        if (BigDecimalUtils.lessThan(fee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("交易金额必须大于0.");
        }
        return this.preOrder(tradeType, tradeNo, productId, body, String.valueOf(fee));
    }

    /**
     * 支付宝退款
     *
     * @param transactionId 支付宝订单号
     * @param tradeNo       商户订单号
     * @param fee
     * @throws Exception
     */
    @Override
    public void refund(String transactionId, String tradeNo, String outNo, String fee) {
        try {
            logger.info("[支付宝退款]-正在发起支付宝退款请求.");
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            //支付宝订单号
            model.setTradeNo(transactionId);
            //商户订单号
            model.setOutTradeNo(tradeNo);
            model.setOutRequestNo(outNo);
            model.setRefundAmount(fee);
            AlipayTradeRefundRequest tradeRefundRequest = new AlipayTradeRefundRequest();
            tradeRefundRequest.setBizModel(model);
            AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey, "json", "UTF-8", alipayPublicKey, SignType.RSA2.name());
            AlipayTradeRefundResponse params = alipayClient.execute(tradeRefundRequest);
            if (!params.isSuccess()) {
                String subMsg = params.getSubMsg();
                if (!("交易已经关闭").equals(subMsg)) {
                    logger.error("[支付宝支付]-支付宝退款失败! 商户订单号：{}, 支付宝交易单号：{}.", tradeNo, transactionId);
                    throw new RuntimeException("[微信支付]-微信退款失败." + subMsg);
                }
                return;
            }
            String timestamp = DateUtils.formatLocalDateTime(DateUtils.date2LocalDate(params.getGmtRefundPay()));//退款成功时间
            String refundAccount = params.getRefundDetailItemList().get(0).getFundChannel();//退回账户
            OrderRefundProtocol protocol = new OrderRefundProtocol(outNo, transactionId, tradeNo, refundAccount, timestamp);
            rocketMQTemplate.getProducer().setProducerGroup("refund-order-success");
            rocketMQTemplate.asyncSend("refund-order-success:refund-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    logger.info("[支付宝支付]-异步投递退款成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                    logger.info("[支付宝支付]-异步投递退款成功订单消息成功,投递结果：{}. ", var);
                }

                @Override
                public void onException(Throwable var) {
                    logger.error("[支付宝支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
                }
            });
            logger.info("[支付宝支付]-支付宝退款成功! 商户退款单号：{}, 商户订单号：{}, 支付宝交易单号：{}, 退款成功时间: {}.", outNo, tradeNo, transactionId, timestamp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 支付宝退款
     */
    @Override
    public void refund(AliRefundRequest refundRequest) {
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
        this.refund(transactionId, tradeNo, outNo, String.valueOf(fee));
    }

    /**
     * 处理支付宝支付回调
     *
     * @return
     */
    public String payNotify(HttpServletRequest request) {
        logger.info("[支付宝]-支付宝发起支付回调请求开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);

        String tradeNo = params.get("out_trade_no"); //商户订单号
        String transactionId = params.get("trade_no"); //支付宝交易号
        String openId = params.get("buyer_id");//买家支付宝用户号
        String fee = params.get("total_amount");//支付金额
        String timestamp = params.get("gmt_payment"); //支付成功时间
        try {
            boolean bool = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", SignType.RSA2.name()); //调用SDK验证签名
            if (!bool) {
                logger.error("[支付宝支付]-支付宝回调验证签名错误!");
                return PayConstants.FAIL;
            }
            //交易状态
            String tradeStatus = params.get("trade_status");

            //退款金额,退款专属字段
            String refundFee = params.get("refund_fee");

            if (tradeStatus.equals(PayConstants.TRADE_FINISHED) || tradeStatus.equals(PayConstants.TRADE_SUCCESS)) {
                if (CharacterUtils.isBlank(refundFee)) {
                    BigDecimal totalFee = BigDecimalUtils.divide(new BigDecimal(fee), new BigDecimal(100));
                    OrderPayProtocol protocol = new OrderPayProtocol(tradeNo, transactionId, totalFee, timestamp);
                    rocketMQTemplate.getProducer().setProducerGroup("trade-order-success");
                    rocketMQTemplate.asyncSend("trade-order-success:trade-order-success", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                        @Override
                        public void onSuccess(SendResult var) {
                            logger.info("[支付宝支付]-异步投递支付成功订单消息成功,订单信息：{}. ", GsonUtils.toJson(protocol));
                            logger.info("[支付宝支付]-异步投递支付成功订单消息成功,投递结果：{}. ", var);
                        }

                        @Override
                        public void onException(Throwable var) {
                            logger.error("[支付宝支付]-异步投递支付成功订单消息失败: 异常信息：{}.", var);
                        }
                    });
                    logger.info("[支付宝支付]-用户公众ID：{} , 商户订单号：{} , 支付宝交易单号：{} 支付宝支付成功！", openId, tradeNo, transactionId);
                } else {
                    logger.info("[支付宝支付]-用户公众ID：{} , 商户订单号：{} , 支付宝交易单号：{} 支付宝退款成功！", 22, tradeNo, transactionId);
                }
            }
            return PayConstants.SUCCESS;
        } catch (Exception e) {
            logger.error("[支付宝支付]-支付宝发起退款回调方法异常! 商户订单号：{}, 支付宝订单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("[支付宝支付]-支付宝发起退款回调方法异常！", e);
        }
    }


    /**
     * 处理支付宝服务器同步通知
     *
     * @return
     */
    public String syncNotify(HttpServletRequest request) {
        logger.info("[支付宝支付]-处理支付宝服务器同步通知开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);
        //商户订单号
        String tradeNo = params.get("out_trade_no");
        //支付宝交易号
        String transactionId = params.get("trade_no");
        try {
            boolean bool = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", SignType.RSA2.name()); //调用SDK验证签名
            if (!bool) {
                logger.error("[支付宝支付]-支付宝服务器同步通知验证签名错误!");
                //out.println("验签失败");
                return PayConstants.FAIL;
            }
            //付款金额
            String totalAmount = params.get("total_amount");
            return PayConstants.SUCCESS;
            //out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
        } catch (Exception e) {
            logger.error("[支付宝支付]-处理支付宝服务器同步通知方法异常,商户订单号：{}. 支付宝订单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("[支付宝支付]-处理支付宝服务器同步通知方法异常！", e);
        }
    }

    public static void main(String[] args) {
        String json = "{\"alipay_trade_refund_response\": { 		\"code\": \"10000\", 		\"msg\": \"Success\", 		\"buyer_logon_id\": \"152******17\", 		\"buyer_user_id\": \"2088512434499773\", 		\"fund_change\": \"Y\", 		\"gmt_refund_pay\": \"2020-01-07 08:45:15\", 		\"out_trade_no\": \"134575161962745857\", 		\"refund_detail_item_list\": [{ 			\"amount\": \"0.01\", 			\"fund_channel\": \"ALIPAYACCOUNT\" 		}], 		\"refund_fee\": \"0.01\", 		\"send_back_fee\": \"0.01\", 		\"trade_no\": \"2020010722001499770526920524\" 	}, 	\"sign\": \"DqD2Rja+0qLcfJUrNgpq0J6p1B6hwleRndHXc68qIAZimx3zNXygAWFeE7EVFmfpm1zZ11E/EY9Q6GlyVBnorAUYlXDniT4KlAOipBuE5NY/k4XKaSks4E1sh/utgwoPYfIFXJqLrrvsOUSNBKKc6d3XbuNgxiMeN/oULTJApOa3o74NMY6uAdYdwRug0mfaNiP4ldS79Bjy63FZ5rWtM8EuFPMPw0OoMze1aACHyFPKLiWD+CTLDubIRi3CRoqVmI9vv2DS8Ubi1qn6N96O75WRVjcc2rzaPmYHFNSVOR5ygFCVpZIWqhhYnVW2Tu+WR0c44Vh8AoGmn/Giji+qcw==\" }";
        AlipayTradeRefundResponse params = GsonUtils.fromJson(json, AlipayTradeRefundResponse.class);
        System.err.println(params);

    }
}
