package org.igetwell.wechat.app.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.igetwell.common.constans.AliPayConstants;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.ParamMap;
import org.igetwell.wechat.app.service.IAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 扫码预付款下单
     *
     * @param body
     * @param fee
     * @return
     */
    @Override
    public String wapPay(String subject, String body, String fee) throws Exception {
        //创建交易信息模型对象
        AlipayTradeWapPayModel precreateModel = new AlipayTradeWapPayModel();
        //商户订单号，需要保证不重复
        precreateModel.setOutTradeNo(attach + sequence.nextNo());
        //订单金额
        precreateModel.setTotalAmount(fee);
        //交易主题
        precreateModel.setSubject(subject);
        //商品名称
        precreateModel.setBody(body);
        //销售产品码，商家和支付宝签约的产品码，该产品请填写固定值：QUICK_WAP_WAY
        precreateModel.setProductCode("QUICK_WAP_PAY");
        //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
        precreateModel.setTimeoutExpress("5m");
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
        String page = alipayClient.pageExecute(alipayTradeWapPayRequest).getBody();
        return page;
    }


    /**
     * PC网站支付
     *
     * @param subject
     * @param body
     * @param fee
     * @return
     * @throws Exception
     */
    @Override
    public String webPc(String subject, String body, String fee) throws Exception {
        //创建交易信息模型对象
        AlipayTradePagePayModel precreateModel = new AlipayTradePagePayModel();
        //商户订单号，需要保证不重复
        precreateModel.setOutTradeNo(attach + sequence.nextNo());
        //订单金额
        precreateModel.setTotalAmount(fee);
        //交易主题
        precreateModel.setSubject(subject);
        //商品名称
        precreateModel.setBody(body);
        //销售产品码，商家和支付宝签约的产品码，该产品请填写固定值：QUICK_WAP_WAY
        precreateModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
        precreateModel.setTimeoutExpress("5m");
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
        String page = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
        return page;
    }

    /**
     * 扫码预付款下单
     *
     * @param subject
     * @param body
     * @param fee
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> scanPay(String subject, String body, String fee) {
        try {
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();//创建API对应的request类
            //商户订单号，需要保证不重复
            model.setOutTradeNo(attach + sequence.nextNo());
            //订单金额
            model.setTotalAmount(fee);
            //交易主题
            model.setSubject(subject);
            //商品名称
            model.setBody(body);
            //销售产品码，不传默认使用FACE_TO_FACE_PAYMENT
            model.setProductCode("FACE_TO_FACE_PAYMENT");
            //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
            model.setTimeoutExpress("5m");
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
            throw new RuntimeException("[支付宝支付]-调用支付宝支付异常.", e);
        }

    }

    /**
     * 支付宝退款
     * @param transactionId 支付宝订单号
     * @param tradeNo 商户订单号
     * @param fee
     * @throws Exception
     */
    @Override
    public void returnPay(String transactionId, String tradeNo, String outNo, String fee) throws Exception {
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
        //tradeRefundRequest.setNotifyUrl(refundNotify);
        //tradeRefundRequest.setReturnUrl(returnNotify);
        AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey,"json","UTF-8", alipayPublicKey, SignType.RSA2.name());
        AlipayTradeRefundResponse response = alipayClient.execute(tradeRefundRequest);
        if(response.isSuccess() && response.getFundChange().equalsIgnoreCase("Y")){
            logger.info("退款成功");
            response.getFundChange();
            response.getGmtRefundPay();
            response.getTradeNo(); //支付宝交易号
            response.getOutTradeNo();//商户订单号
        }
    }

    /**
     * 处理支付宝支付回调
     *
     * @return
     */
    public String notifyMethod(HttpServletRequest request) {
        logger.info("[支付宝支付]-支付宝支付回调请求开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);
        //商户订单号
        String tradeNo = params.get("out_trade_no");
        //支付宝交易号
        String transactionId = params.get("trade_no");
        try {
            boolean bool = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", SignType.RSA2.name()); //调用SDK验证签名
            if (!bool) {
                logger.error("[支付宝支付]-支付宝回调验证签名错误!");
                return AliPayConstants.FAIL;
            }
            //交易状态
            String tradeStatus = params.get("trade_status");

            //退款金额,退款专属字段
            String refundFee = params.get("refund_fee");

            if (tradeStatus.equals(AliPayConstants.TRADE_FINISHED ) || tradeStatus.equals(AliPayConstants.TRADE_SUCCESS)) {
                if (CharacterUtils.isBlank(refundFee)) {
                    logger.info("[支付宝支付]-用户公众ID：{} , 订单号：{} , 交易号：{} 支付宝支付成功！", 11, tradeNo, transactionId);
                } else {
                    logger.info("[支付宝支付]-用户公众ID：{} , 订单号：{} , 交易号：{} 支付宝退款成功！", 22, tradeNo, transactionId);
                }
            }
            logger.info("[支付宝支付]-进入支付宝支付回调结束.");
            return AliPayConstants.SUCCESS;
        } catch (Exception e) {
            logger.error("[支付宝支付]-调用支付宝支付回调方法异常,商户订单号：{}. 支付宝订单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("[支付宝支付]-调用支付宝支付回调方法异常！", e);
        }
    }


    /**
     * 处理支付宝服务器同步通知
     * @return
     */
    public String returnMethod(HttpServletRequest request) {
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
                return AliPayConstants.FAIL;
            }
            //付款金额
            String totalAmount = params.get("total_amount");
            return AliPayConstants.SUCCESS;
            //out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
        } catch (Exception e) {
            logger.error("[支付宝支付]-处理支付宝服务器同步通知方法异常,商户订单号：{}. 支付宝订单号：{}. ", tradeNo, transactionId, e);
            throw new RuntimeException("[支付宝支付]-处理支付宝服务器同步通知方法异常！", e);
        }
    }
}
