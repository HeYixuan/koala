package org.igetwell.union.service.impl;

import org.igetwell.common.enums.SignType;
import org.igetwell.common.uitls.*;
import org.igetwell.union.service.IUnionPayService;
import org.igetwell.union.service.impl.unionpay.acp.sdk.AcpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UnionPayService implements IUnionPayService {

    private static final Logger logger = LoggerFactory.getLogger(UnionPayService.class);

   // @Value("${union.appId}")
    private String defaultAppId = "777290058177815";
    //@Value("${union.payNotify}")
    private String payNotify = "http://222.222.222.222:8080/ACPSample_B2C/backRcvResponse";
   // @Value("${union.refundNotify}")
    private String refundNotify;
    //@Value("${union.paterKey}")
    private String paterKey;

    /**
     * 手机网站支付预付款下单
     */
    public Map<String, String> wap(String tradeNo, String subject, String body, String fee) {
        Map<String,String> paraMap= ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 07：申请消费二维码
                .put("bizType", "000201") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
    /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFromData = AcpService.sign(paraMap);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
        String html = AcpService.createAutoFormHtml(frontTransUrl, submitFromData, "UTF-8");   //生成自动跳转的Html表单
        paraMap.put("page", html);
        return paraMap;
    }

    /**
     * PC网站支付
     */
    public Map<String, String> web(String tradeNo, String subject, String body, String fee) {
        Map<String,String> paraMap= ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 07：申请消费二维码
                .put("bizType", "000201") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "07") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        logger.info("=================统一下单调用开始====================");
        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFromData = AcpService.sign(params);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
        String html = AcpService.createAutoFormHtml(frontTransUrl, submitFromData, "UTF-8");   //生成自动跳转的Html表单
        paraMap.put("page", html);
        return paraMap;
    }


    /**
     * 扫码预付款下单(用户主扫)
     */
    public Map<String, String> sca(String tradeNo, String subject, String body, String fee) {
        Map<String,String> paraMap= ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "01") //交易子类 01：统一下单
                .put("bizType", "000000") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        logger.info("=================统一下单调用开始====================");
        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFromData = AcpService.sign(params);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/order.do";
        Map<String, String> resultMap = AcpService.post(submitFromData,frontTransUrl, "UTF-8");
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))){
            throw new RuntimeException("[银联支付]-发起银联支付失败");
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
        Map<String,String> paraMap= ParamMap.create("tradeNo", tradeNo)
                .put("fee", fee)
                .put("txnSubType", "07") //交易子类 07：申请消费二维码
                .put("bizType", "000000") //业务类型，000201 B2C网关支付，手机wap支付, 000000用户主动扫码
                .put("channelType", "08") //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
                .getData();
        Map<String, String> params = createParams(paraMap, SignType.MD5);
        logger.info("=================统一下单调用开始====================");
        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFromData = AcpService.sign(params);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String frontTransUrl = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
        Map<String, String> resultMap = AcpService.post(submitFromData,frontTransUrl, "UTF-8");
        if (resultMap.isEmpty() || !"00".equals(resultMap.get("respCode"))){
            throw new RuntimeException("[银联支付]-发起银联支付失败");
        }
        String codeUrl = resultMap.get("qrCode");
        Map<String, String> packageMap = new HashMap<>();
        packageMap.put("codeUrl", codeUrl);
        return packageMap;
    }

    /**
     * 签名入参
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
        params.put("tradeType", "mobileWeb");          		 	//交易场景 固定填写	mobileWeb
        /*String sign = SignUtils.createSign(params, paterKey, signType);
        params.put("signature", sign);*/

        return params;
    }

    /**
     * 预支付接口
     * @param hashMap
     * @param signType
     * @return
     * @throws Exception
     */
    private Map<String, String> prePay(Map<String, String> hashMap, SignType signType) throws Exception{
        hashMap.put("mchId", defaultAppId); //  商户号
        // 创建请求参数
        Map<String, String> params = createParams(hashMap, signType);
        logger.info("=================统一下单调用开始====================");
        // 微信统一下单
        /*String result = pushOrder(params);
        Map<String, String> resultXml = BeanUtils.xml2Map(result);
        this.parseXml(resultXml);
        return resultXml;*/
        return null;
    }
}
