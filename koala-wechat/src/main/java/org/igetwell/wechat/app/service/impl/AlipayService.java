package org.igetwell.wechat.app.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.wechat.app.service.IAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayService implements IAlipayService {


    @Value("${attach}")
    private String attach;
    @Value("${alipay.gateway}")
    private String gateway;
    @Value("${alipay.payNotify}")
    private String payNotify;
    @Value("${alipay.returnUrl}")
    private String returnUrl;
    @Value("${alipay.refundNotify}")
    private String refundNotify;
    @Autowired
    private Sequence sequence;

    /**
     * 扫码预付款下单
     * @param productName
     * @param productId
     * @param fee
     * @return
     */
    @Override
    public String wapPay(String subject, String productName, String productId, String fee) throws Exception {
        //创建阿里客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, SignType.RSA2.name());
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
        //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body
        precreateModel.setSellerId(productId);
        //销售产品码，商家和支付宝签约的产品码，该产品请填写固定值：QUICK_WAP_WAY
        precreateModel.setProductCode("QUICK_WAP_WAY");
        //设置支付宝交易超时 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
        precreateModel.setTimeoutExpress("5m");
        //创建阿里请求对象
        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
        //业务请求参数的集合
        alipayTradeWapPayRequest.setBizModel(precreateModel);
        //设置后台异步通知的地址，在手机端支付后支付宝会通知后台(成功或者失败)，手机端的真实支付结果依赖于此地址
        alipayTradeWapPayRequest.setNotifyUrl(payNotify);
        //支付成功后的跳转页面,由于前台回跳的不可靠性，前台回跳只能作为商户支付结果页的入口，最终支付结果必须以异步通知或查询接口返回为准，不能依赖前台回跳
        alipayTradeWapPayRequest.setReturnUrl(returnUrl);
        String page = alipayClient.pageExecute(alipayTradeWapPayRequest).getBody();
        return page;
    }
}
