package org.igetwell.union.sdk;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
/***
 * 银联加密算法工具类
 */
public class SecureUtils {

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
	 * @param data
	 *            待计算的数据
	 * @return
	 */
	private static byte[] sha256(String data) {
		try {
			return sha256(data.getBytes("UTF-8"));
		} catch (Exception e) {
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
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA256);
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
		try {
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
			return sha256StrBuff.toString().getBytes("UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] signBySoft256(PrivateKey privateKey, byte[] data) throws Exception {
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initSign(privateKey);
		st.update(data);
		byte[] result = st.sign();
		return result;
	}

	public static boolean validateSignBySoft256(PublicKey publicKey, byte[] signData, byte[] srcData) throws Exception {
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}
}