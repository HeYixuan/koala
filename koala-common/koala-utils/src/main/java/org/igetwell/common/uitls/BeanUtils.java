package org.igetwell.common.uitls;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.lang.reflect.Field;
import java.util.*;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanUtils {

    /**
     * 将bean按照@XStreamAlias标识的字符串内容生成以之为key的map对象
     *
     * @param bean 包含@XStreamAlias的xml bean对象
     * @return map对象
     */
    public static Map<String, String> xmlBean2Map(Object bean) {
        Map<String, String> result = new HashMap<>();
        List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        for (Field field : fields) {
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    field.setAccessible(isAccessible);
                    continue;
                }
                if (field.isAnnotationPresent(XStreamAlias.class)) {
                    result.put(field.getAnnotation(XStreamAlias.class).value(), field.get(bean).toString());
                }
                field.setAccessible(isAccessible);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return result;
    }


    /**
     * 将map转成xml
     * @param params
     * @return
     */
    public static String mapBean2Xml(Map<String, String> params) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        Iterator var2 = params.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (!StringUtils.hasText(value)) {
                xml.append("<").append(key).append(">");
                xml.append((String)entry.getValue());
                xml.append("</").append(key).append(">");
            }
        }

        xml.append("</xml>");
        return xml.toString();
    }


    /**
     * 将xml转成map
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xmlBean2Map(String xmlStr) {
        Document document = XmlUtils.parse(xmlStr);
        Element root = document.getDocumentElement();
        Map<String, String> params = new HashMap();
        NodeList list = root.getChildNodes();
        for(int i = 0; i < list.getLength(); ++i) {
            Node node = list.item(i);
            params.put(node.getNodeName(), node.getTextContent());
        }

        params.remove("#text");
        return params;
    }
}
