package org.igetwell.common.uitls;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtils {
    /**
     * 通过xml字符获取document文档
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document getDocument(String xml) {
        if(xml=="" || xml==null){
            return null;
        }
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }


    public final static Element parseXml(String xml) {
        StringReader sr = new StringReader(xml);
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(sr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document != null ? document.getRootElement() : null;
        return rootElement;
    }


    public static Element element(Element element, String name) {
        Element _e = element.element(name);
        if (_e != null)
            return _e;
        else
            return null;
    }

    /**
     * 获取element对象的text的值
     * @param element
     * @param name
     * @return
     */
    public final static String elementText(Element element, String name) {
        Element _e = element.element(name);
        if (_e != null)
            return _e.getText();
        else
            return null;
    }

    /**
     * 获取指定节点下所有子节点的属性集合
     * @param element
     * @return
     */
    public static Map<String, Object> getAttrMap(Element element){
        Map<String, Object> allAttrMap = new HashMap<String, Object>();
        if(element == null)
            return null;
        List<Element> list = element.elements();
        for (int i = 0; i < list.size(); ++i) {
            Element node = list.get(i);
            allAttrMap.put(node.getName(), node.getText());
        }
        return allAttrMap;
    }

    /**
     * 获取指定节点下所有子节点的属性集合
     * @param element
     * @return
     */
    public static Map<String, String> getAttr(Element element){
        Map<String, String> allAttrMap = new HashMap<String, String>();
        if(element == null)
            return null;
        List<Element> list = element.elements();
        for (int i = 0; i < list.size(); ++i) {
            Element node = list.get(i);
            allAttrMap.put(node.getName(), node.getText());
        }
        return allAttrMap;
    }


    public static void main(String [] args) throws DocumentException {
        //https://blog.csdn.net/progammer10086/article/details/94737570
        String xml = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe> <mch_id><![CDATA[10000100]]></mch_id> <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str> <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[1409811653]]></out_trade_no> <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code> <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign> <sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end> <total_fee>1</total_fee> <coupon_fee_0><![CDATA[10]]></coupon_fee_0> <coupon_count><![CDATA[1]]></coupon_count> <coupon_type><![CDATA[CASH]]></coupon_type> <coupon_id><![CDATA[10000]]></coupon_id> <trade_type><![CDATA[JSAPI]]></trade_type> <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id> </xml>";

        Document document = getDocument(xml);
        Element element = document.getRootElement();
        Element _e = element.element("attach");
        String appid = elementText(element,"appid");
        System.err.println(appid);
        System.err.println(_e.getText());

        Map<String, Object> map = BeanUtils.xmlBean2Map(xml);
        System.err.println(GsonUtils.toJson(map));
    }

}
