package org.igetwell.common.uitls;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;
import java.util.Arrays;

/**
 * 检验签名
 */
public class CheckSignature {

    // 与接口配置信息中的Token要一致
    private static String COMPONENT_TOKEN = "HeYixuan";

    /**
     * 微信验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)){
            return false;
        }
        String[] arr = new String[] { COMPONENT_TOKEN, timestamp, nonce };
        Arrays.sort(arr);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
        }
        return DigestUtils.sha1Hex(sb.toString()).equalsIgnoreCase(signature);
    }

}
