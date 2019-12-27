package org.igetwell.wechat.app.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.igetwell.common.constans.AliPayConstants;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ParamMap;
import org.igetwell.wechat.app.service.IAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    @Override
    public String wapPay(String subject, String productName, String productId, String fee) throws Exception {
        //创建交易信息模型对象
        AlipayTradeWapPayModel precreateModel = new AlipayTradeWapPayModel();
        //商户订单号，需要保证不重复
        precreateModel.setOutTradeNo(attach + sequence.nextNo());
        //订单金额
        precreateModel.setTotalAmount(fee);
        //交易主题
        precreateModel.setSubject(subject);
        //商品名称
        precreateModel.setBody(productName);
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
     * @param productName
     * @param productId
     * @param fee
     * @return
     * @throws Exception
     */
    @Override
    public String webPc(String subject, String productName, String productId, String fee) throws Exception {
        //创建交易信息模型对象
        AlipayTradePagePayModel precreateModel = new AlipayTradePagePayModel();
        //商户订单号，需要保证不重复
        precreateModel.setOutTradeNo(attach + sequence.nextNo());
        //订单金额
        precreateModel.setTotalAmount(fee);
        //交易主题
        precreateModel.setSubject(subject);
        //商品名称
        precreateModel.setBody(productName);
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
     * @param productName
     * @param productId
     * @param fee
     * @return
     * @throws Exception
     */
    @Override
    public String scanPay(String subject, String productName, String productId, String fee) throws Exception {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();//创建API对应的request类
        //商户订单号，需要保证不重复
        model.setOutTradeNo(attach + sequence.nextNo());
        //订单金额
        model.setTotalAmount(fee);
        //交易主题
        model.setSubject(subject);
        //商品名称
        model.setBody(productName);
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
        String qrUrl = alipayClient.execute(request).getQrCode();
        return qrUrl;
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
            /* 实际验证过程建议商户务必添加以下校验：
                1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
                2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
                3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
                4、验证app_id是否为该商户本身。
            */
            if (!bool) {
                logger.error("[支付宝支付]-支付宝回调验证签名错误!");
                return AliPayConstants.FAIL;
            }
            //交易状态
            String tradeStatus = params.get("trade_status");

            if (tradeStatus.equals(AliPayConstants.TRADE_FINISHED ) || tradeStatus.equals(AliPayConstants.TRADE_SUCCESS)) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                logger.info("[支付宝支付]-用户公众ID：{} , 订单号：{} , 交易号：{} 支付宝支付成功！", 11, tradeNo, transactionId);
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
