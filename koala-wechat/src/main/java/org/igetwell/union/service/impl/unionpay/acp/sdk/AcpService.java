package org.igetwell.union.service.impl.unionpay.acp.sdk;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @ClassName AcpService
 * @Description acpsdk接口服务类，接入商户集成请可以直接参考使用本类中的方法
 * @date 2016-7-22 下午2:44:37
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
@Slf4j
public class AcpService {

	/**
	 * 请求报文签名(使用配置文件中配置的私钥证书或者对称密钥签名)<br>
	 * 功能：对请求报文进行签名,并计算赋值certid,signature字段并返回<br>
	 * @param paramsMap 请求报文map<br>
	 * @return　签名后的map对象<br>
	 */
	public static Map<String, String> sign(Map<String, String> paramsMap) {
		paramsMap = SDKUtil.filterBlank(paramsMap);
		SDKUtil.sign(paramsMap);
		return paramsMap;
	}

	/**
	 * 功能：后台交易提交请求报文并接收同步应答报文<br>
	 * @param reqData 请求报文<br>
	 * @param reqUrl  请求地址<br>
	 * @param encoding<br>
	 * @return 应答http 200返回true ,其他false<br>
	 */
	public static Map<String,String> post(Map<String, String> reqData, String reqUrl,String encoding) {
		Map<String, String> rspData = new HashMap<String,String>();
		log.info("请求银联地址:" + reqUrl);
		//发送后台请求数据
		HttpClient hc = new HttpClient(reqUrl, 30000, 30000);//连接超时时间，读超时时间（可自行判断，修改）
		try {
			int status = hc.send(reqData, encoding);
			if (200 == status) {
				String resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					// 将返回结果转换为map
					Map<String,String> tmpRspData  = SDKUtil.convertResultStringToMap(resultString);
					rspData.putAll(tmpRspData);
				}
			}else{
				log.info("返回http状态码["+status+"]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return rspData;
	}

	/**
	 * 功能：前台交易构造HTTP POST自动提交表单<br>
	 * @param reqUrl 表单提交地址<br>
	 * @param hiddens 以MAP形式存储的表单键值<br>
	 * @param encoding 上送请求报文域encoding字段的值<br>
	 * @return 构造好的HTTP POST交易表单<br>
	 */
	public static String createAutoFormHtml(String reqUrl, Map<String, String> hiddens,String encoding) {
		StringBuffer sf = new StringBuffer();
		sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"/></head><body>");
		sf.append("<form id = \"pay_form\" action=\"" + reqUrl
				+ "\" method=\"post\">");
		if (null != hiddens && 0 != hiddens.size()) {
			Set<Map.Entry<String, String>> set = hiddens.entrySet();
			Iterator<Map.Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> ey = it.next();
				String key = ey.getKey();
				String value = ey.getValue();
				sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
						+ key + "\" value=\"" + value + "\"/>");
			}
		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.all.pay_form.submit();");
		sf.append("</script>");
		sf.append("</html>");
		return sf.toString();
	}
}
