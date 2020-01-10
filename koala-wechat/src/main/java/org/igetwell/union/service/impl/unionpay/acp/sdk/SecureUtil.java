/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28     报文加密解密等操作的工具类
 * =============================================================================
 */
package org.igetwell.union.service.impl.unionpay.acp.sdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;
/**
 * 
 * @ClassName SecureUtil
 * @Description acpsdk安全算法工具类
 * @date 2016-7-22 下午4:08:32
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class SecureUtil {
	/**
	 * 算法常量： SHA1
	 */
	private static final String ALGORITHM_SHA1 = "SHA-1";
	/**
	 * 算法常量： SHA256
	 */
	private static final String ALGORITHM_SHA256 = "SHA-256";
	/**
	 * 算法常量：SHA1withRSA
	 */
	private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";
	/**
	 * 算法常量：SHA256withRSA
	 */
	private static final String BC_PROV_ALGORITHM_SHA256RSA = "SHA256withRSA";

	/**
	 * sha256计算
	 *
	 * @param datas
	 *            待计算的数据
	 * @return
	 */
	private static byte[] sha256(String datas) {
		try {
			return sha256(datas.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * sha256计算.
	 *
	 * @param data
	 *            待计算的数据
	 * @return 计算结果
	 */
	private static byte[] sha256(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM_SHA256);
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * sha256计算后进行16进制转换
	 *
	 * @param data
	 *            待计算的数据
	 * @return 计算结果
	 */
	public static String sha256X16Str(String data) {
		byte[] bytes = sha256(data);
		StringBuilder sha256StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha256StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sha256StrBuff.toString();
	}

	/**
	 * sha256计算后进行16进制转换
	 *
	 * @param data
	 *            待计算的数据
	 *            编码
	 * @return 计算结果
	 */
	public static byte[] sha256X16(String data) {
		byte[] bytes = sha256(data);
		StringBuilder sha256StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha256StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		try {
			return sha256StrBuff.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] signBySoft256(PrivateKey privateKey, byte[] data)
			throws Exception {
		byte[] result = null;
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initSign(privateKey);
		st.update(data);
		result = st.sign();
		return result;
	}

	public static boolean validateSignBySoft256(PublicKey publicKey, byte[] signData, byte[] srcData) throws Exception {

		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}

	public static boolean validateSignBySoft(PublicKey publicKey,
											 byte[] signData, byte[] srcData) throws Exception {
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}
}