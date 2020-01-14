package org.igetwell.union.sdk;

import org.igetwell.common.uitls.Base64Utils;
import org.igetwell.common.uitls.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.X509Certificate;
import java.util.*;


/**
 * 银联签名工具类
 */
public class UnionSignUtils {

    protected static final Logger logger = LoggerFactory.getLogger(UnionSignUtils.class);

    /** 证书ID. */
    public static final String CERT_ID = "certId";
    /** 签名. */
    public static final String SIGN = "signature";
    /** 签名公钥证书 */
    public static final String SIGN_PUB_KEY_CERT = "signPubKeyCert";

    /**
     * 根据signMethod的值，提供三种计算签名的方法
     *
     * @param params
     *            待签名数据Map键值对形式
     *            编码
     * @return 签名是否成功
     */
    public static String createSign(Map<String, String> params) {
        try {
            // 设置签名证书序列号
            params.put(CERT_ID, CertUtils.getCertId());
            params.remove(SIGN); //防止sign作参数,移除sign
            String result = ParamMap.coverMap2String(params);
            // 通过SHA256进行摘要并转16进制
            byte[] signDigest = SecureUtils.sha256X16(result);
            logger.info("打印摘要（交易返回11验证签名失败可以用来同正确的进行比对）:[" + new String(signDigest) + "]");
            String sign = Base64Utils.base64Encode(SecureUtils.signBySoft256(CertUtils.getPrivateKey(), signDigest));
            return sign;
        } catch (Exception e) {
            logger.error("签名错误.", e);
            return null;
        }
    }

    /**
     * 验签
     * @return
     */
    public static boolean checkSign(Map<String, String> params) {
        // 1.从返回报文中获取公钥信息转换成公钥对象
        String cert = params.get(SIGN_PUB_KEY_CERT);
        X509Certificate x509Cert = CertUtils.getCertificate(cert);
        if (x509Cert == null) {
            logger.error("获取签名公钥失败");
            return false;
        }
        // 2.验证证书链
        if (!CertUtils.verifyCertificate(x509Cert)) {
            logger.error("验证公钥证书失败，证书信息：[" + cert + "]");
            return false;
        }

        // 3.验签
        String sign = params.get(SIGN);
        logger.info("签名原文：[" + sign + "]");
        // 将Map信息转换成key1=value1&key2=value2的形式
        params.remove(SIGN); //防止sign作参数,移除sign
        String paramStr = ParamMap.coverMap2String(params);
        logger.info("待验签返回报文串：[" + paramStr + "]");
        try {
            // 验证签名需要用银联发给商户的公钥证书.
            boolean bool = SecureUtils.validateSignBySoft256(x509Cert.getPublicKey(), Base64Utils.decodeBase64(sign.getBytes("UTF-8")), SecureUtils.sha256X16(paramStr));
            logger.info("验证签名" + (bool ? "成功" : "失败"));
            return bool;
        } catch (Exception e) {
            logger.error("验证签名失败.", e);
            return false;
        }
    }

}
