package org.igetwell.common.uitls;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

public class AESDecodeUtil {

    public static final String ALGORITHM = "AES/ECB/PKCS7Padding";

    public static String decode(String text, String key) {
        try {
            byte[] decodeBase64 = Base64.decodeBase64(text);
            String md5Key = DigestUtils.md5Hex(key);
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey keySpec = new SecretKeySpec(md5Key.getBytes(), "AES"); //生成加密解密需要的Key
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(decodeBase64);
            String result = new String(decoded, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
