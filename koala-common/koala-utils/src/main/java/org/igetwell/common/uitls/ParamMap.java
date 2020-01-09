package org.igetwell.common.uitls;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    /**
     * 将URL的参数和body参数合并
     */
    public static Map<String, String> getParams(final HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        //获取URL上的参数
        Map<String, String> urlParams = getParameterMap(request);
        for (Map.Entry entry : urlParams.entrySet()) {
            params.put((String) entry.getKey(), (String) entry.getValue());
        }
        Map<String, Object> bodyParams = new HashMap<>(16);
        // get请求不需要拿body参数
        if (!HttpMethod.GET.name().equals(request.getMethod())) {
            bodyParams = getBodyParams(request);
        }
        //将URL的参数和body参数进行合并
        if (bodyParams != null) {
            for (Map.Entry entry : bodyParams.entrySet()) {
                params.put((String) entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return params;
    }

    /**
     * 获取 Body 参数
     */
    public static Map<String, Object> getBodyParams(final HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return GsonUtils.fromJson(body, Map.class);
    }

    /**
     * 将URL请求参数转换成Map
     */
    public static Map<String, String> getUrlParams(final HttpServletRequest request) {
        return null;
    }
}
