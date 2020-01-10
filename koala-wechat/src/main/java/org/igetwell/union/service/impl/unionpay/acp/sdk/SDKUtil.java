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
 *   xshu       2014-05-28      MPI工具类
 * =============================================================================
 */
package org.igetwell.union.service.impl.unionpay.acp.sdk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.igetwell.union.service.impl.CertUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static org.igetwell.union.service.impl.unionpay.acp.sdk.SDKConstants.*;

/**
 * 
 * @ClassName SDKUtil
 * @Description acpsdk工具类
 * @date 2016-7-22 下午4:06:18
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
@Slf4j
public class SDKUtil {

	/**
	 * 根据signMethod的值，提供三种计算签名的方法
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 *            编码
	 * @return 签名是否成功
	 */
	public static boolean sign(Map<String, String> data) {
		// 设置签名证书序列号
		data.put(SDKConstants.param_certId, CertUtils.getCertId());
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA256进行摘要并转16进制
			byte[] signDigest = SecureUtil.sha256X16(stringData);
			log.info("打印摘要（交易返回11验证签名失败可以用来同正确的进行比对）:[" + new String(signDigest)+ "]");
			byteSign = Base64.encodeBase64(SecureUtil.signBySoft256(CertUtils.getPrivateKey("000000"), signDigest));
			stringSign = new String(byteSign);
			// 设置签名域值
			data.put(SDKConstants.param_signature, stringSign);
			return true;
		} catch (Exception e) {
			log.error("Sign Error", e);
			return false;
		}
	}


	/**
	 * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (SDKConstants.param_signature.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue()
					+ SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}


	/**
	 * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> coverResultString2Map(String result) {
		return convertResultStringToMap(result);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map = null;

		if (result != null && !"".equals(result.trim())) {
			if (result.startsWith("{") && result.endsWith("}")) {
				result = result.substring(1, result.length() - 1);
			}
			map = parseQString(result);
		}

		return map;
	}

	
	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseQString(String str) {

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;//值里有嵌套
		char openName = 0;
		if(len>0){
			for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey) {// 如果当前生成的是key
					
					if (curChar == '=') {// 如果读取到=分隔符 
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else  {// 如果当前生成的是value
					if(isOpen){
						if(curChar == openName){
							isOpen = false;
						}
						
					}else{//如果没开启嵌套
						if(curChar == '{'){//如果碰到，就开启嵌套
							isOpen = true;
							openName ='}';
						}
						if(curChar == '['){
							isOpen = true;
							openName =']';
						}
					}
					
					if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					} else {
						temp.append(curChar);
					}
				}
				
			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
			String key, Map<String, String> map) {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}


	
	/**
	 * 文件拷贝方法
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFile, String destFile) {
		boolean flag = false;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			// 获取源文件和目标文件的输入输出流
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(destFile);
			// 获取输入输出通道
			fcin = fin.getChannel();
			fcout = fout.getChannel();
			// 创建缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (true) {
				// clear方法重设缓冲区，使它可以接受读入的数据
				buffer.clear();
				// 从输入通道中将数据读到缓冲区
				int r = fcin.read(buffer);
				// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
				if (r == -1) {
					flag = true;
					break;
				}
				// flip方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();
				// 从输出通道中将数据写入缓冲区
				fcout.write(buffer);
			}
			fout.flush();
		} catch (IOException e) {
            log.error("CopyFile fail", e);
		} finally {
			try {
				if (null != fin)
					fin.close();
				if (null != fout)
					fout.close();
				if (null != fcin)
					fcin.close();
				if (null != fcout)
					fcout.close();
			} catch (IOException ex) {
                log.error("Releases any system resources fail", ex);
			}
		}
		return flag;
	}
	
	/**
	 * 写文件方法
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static boolean writeFile(String filePath, String fileContent,
			String encoding) {
		FileOutputStream fout = null;
		FileChannel fcout = null;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			fout = new FileOutputStream(filePath);
			// 获取输出通道
			fcout = fout.getChannel();
			// 创建缓冲区
			// ByteBuffer buffer = ByteBuffer.allocate(1024);
			ByteBuffer buffer = ByteBuffer.wrap(fileContent.getBytes(encoding));
			fcout.write(buffer);
			fout.flush();
		} catch (FileNotFoundException e) {
            log.error("WriteFile fail", e);
			return false;
		} catch (IOException ex) {
            log.error("WriteFile fail", ex);
			return false;
		} finally {
			try {
				if (null != fout)
					fout.close();
				if (null != fcout)
					fcout.close();
			} catch (IOException ex) {
                log.error("Releases any system resources fail", ex);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 将传入的文件名(xxx)改名 <br>
	 * 结果为： xxx_backup.cer
	 * 
	 * @param fileName
	 * @return
	 */
	public static String genBackupName(String fileName) {
		if (isEmpty(fileName))
			return "";
		int i = fileName.lastIndexOf(POINT);
		String leftFileName = fileName.substring(0, i);
		String rightFileName = fileName.substring(i + 1);
		String newFileName = leftFileName + "_backup" + POINT + rightFileName;
		return newFileName;
	}
	

	public static byte[] readFileByNIO(String filePath) {
		FileInputStream in = null;
		FileChannel fc = null;
		ByteBuffer bf = null;
		try {
			in = new FileInputStream(filePath);
			fc = in.getChannel();
			bf = ByteBuffer.allocate((int) fc.size());
			fc.read(bf);
			return bf.array();
		} catch (Exception e) {
            log.error(e.getMessage());
			return null;
		} finally {
			try {
				if (null != fc) {
					fc.close();
				}
				if (null != in) {
					in.close();
				}
			} catch (Exception e) {
                log.error(e.getMessage());
				return null;
			}
		}
	}
	
	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * @param contentData
	 * @return
	 */
	public static Map<String, String> filterBlank(Map<String, String> contentData){
        log.info("打印请求报文域 :");
		Map<String, String> submitFromData = new HashMap<String, String>();
		Set<String> keyset = contentData.keySet();
		for(String key:keyset){
			String value = contentData.get(key);
			if (value != null && !"".equals(value.trim())) {
				// 对value值进行去除前后空处理
				submitFromData.put(key, value.trim());
                log.info(key + "-->" + String.valueOf(value));
			}
		}
		return submitFromData;
	}
	
	/**
	 * 解压缩.
	 * 
	 * @param inputByte
	 *            byte[]数组类型的数据
	 * @return 解压缩后的数据
	 * @throws IOException
	 */
	public static byte[] inflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (Exception ex) {
			System.err.println("Data format error!\n");
			ex.printStackTrace();
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

	/**
	 * 压缩.
	 * 
	 * @param inputByte
	 *            需要解压缩的byte[]数组
	 * @return 压缩后的数据
	 * @throws IOException
	 */
	public static byte[] deflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}
	
	/**
	 * 判断字符串是否为NULL或空
	 * 
	 * @param s
	 *            待判断的字符串数据
	 * @return 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}
	
}
