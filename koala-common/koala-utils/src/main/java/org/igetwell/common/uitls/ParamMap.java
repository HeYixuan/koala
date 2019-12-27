package org.igetwell.common.uitls;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParamMap {
    private Map<String, String> data = new ConcurrentHashMap<String, String>();

    private ParamMap() {
    }

    public static ParamMap create() {
        return new ParamMap();
    }

    public static ParamMap create(String key, String value) {
        return create().put(key, value);
    }

    public ParamMap put(String key, String value) {
        this.data.put(key, value);
        return this;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> params = new ConcurrentHashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String value = "";
            for (int i = 0; i < values.length; i++) {
                value = (i == values.length - 1) ? value + values[i]
                        : value + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(value.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, value);
        }
        return params;
    }
}
