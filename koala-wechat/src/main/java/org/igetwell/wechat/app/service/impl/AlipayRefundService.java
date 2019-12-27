package org.igetwell.wechat.app.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ParamMap;
import org.igetwell.wechat.app.service.IAlipayRefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class AlipayRefundService implements IAlipayRefundService {

    private static final Logger logger = LoggerFactory.getLogger(AlipayRefundService.class);

    @Autowired
    private Sequence sequence;


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
        tradeRefundRequest.setNotifyUrl(refundNotify);
        tradeRefundRequest.setReturnUrl(returnNotify);
        AlipayClient alipayClient = new DefaultAlipayClient(gateway, appId, privateKey,"json","UTF-8", alipayPublicKey, SignType.RSA2.name());
        AlipayTradeRefundResponse response = alipayClient.execute(tradeRefundRequest);
        if(response.isSuccess() && response.getFundChange().equalsIgnoreCase("Y")){
            logger.info("退款成功");
            response.getFundChange();
            response.getGmtRefundPay();
            response.getTradeNo(); //支付宝交易号
            response.getOutTradeNo();//商户订单号
        } else {

        }
    }

    /**
     * 处理支付宝退款回调
     * @return
     */
    public String notifyMethod(HttpServletRequest request) {
        logger.info("[支付宝退款]-支付宝退款回调请求开始.");
        Map<String, String> params = ParamMap.getParameterMap(request);
        //商户订单号
        String tradeNo = params.get("out_trade_no");
        //支付宝交易号
        String transactionId = params.get("trade_no");
        return "success";
    }
}
