package org.igetwell.common.uitls;

import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.igetwell.common.enums.SignType;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignUtils {


    /**
     * JsTicket签名算法(详见:https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=4_3)
     * @param params  参数信息
     * @return 签名字符串
     */
    public static String createJsTicketSign(final Map<String, String> params) {
        SortedMap<String, String> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            if (!CharacterUtils.isBlank(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(params.get(key).trim()).append("&");
            }
        }
        return DigestUtils.sha1Hex(toSign.toString()).toUpperCase();
    }

    /**
     * 微信公众号支付签名算法(详见:https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=4_3)
     *
     * @param xmlBean Bean需要标记有XML注解
     * @param signKey 签名Key
     * @return 签名字符串
     */
    public static String createSign(Object xmlBean, String signKey) {
        return createSign(BeanUtils.xmlBean2Map(xmlBean), signKey);
    }


    /**
     * 微信公众号支付签名算法(详见:https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=4_3)
     *
     * @param params  参数信息
     * @param signKey 签名Key
     * @return 签名字符串
     */
    public static String createSign(final Map<String, String> params, String signKey) {

        SortedMap<String, String> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            if (!CharacterUtils.isBlank(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(params.get(key).trim()).append("&");
            }
        }

        toSign.append("key=" + signKey);
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param params 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String createSign(final Map<String, String> params, String key, SignType signType) throws Exception {
        params.remove("sign"); //防止sign作参数,移除sign
        Set<String> keySet = params.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (!CharacterUtils.isBlank(params.get(k)) && params.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(params.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return DigestUtils.md5Hex(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 校验签名是否正确
     *
     * @param xmlBean Bean需要标记有XML注解
     * @param signKey 校验的签名Key
     * @return true - 签名校验成功，false - 签名校验失败
     * @see #checkSign(Map, String)
     */
    public static boolean checkSign(Object xmlBean, String signKey) {
        return checkSign(BeanUtils.xmlBean2Map(xmlBean), signKey);
    }

    /**
     * 校验签名是否正确
     *
     * @param params  需要校验的参数Map
     * @param signKey 校验的签名Key
     * @return true - 签名校验成功，false - 签名校验失败
     * @see #checkSign(Map, String)
     */
    public static boolean checkSign(Map<String, String> params, String signKey) {
        String sign = createSign(params, signKey);
        return sign.equals(params.get("sign"));
    }

    /**
     * 校验签名是否正确.
     *
     * @param params   需要校验的参数Map
     * @param signType 签名类型，如果为空，则默认为MD5
     * @param signKey  校验的签名Key
     * @return true - 签名校验成功，false - 签名校验失败
     */
    public static boolean checkSign(Map<String, String> params, String signKey, SignType signType) throws Exception {
        String sign = params.get("sign");
        String localSign = createSign(params, signKey, signType);
        return sign.equals(localSign);
    }


    /**
     * 生成 HMACSHA256
     * @param message 待处理数据
     * @param key API密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String message, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
        //return Hex.encodeHexString(array).toUpperCase();
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
