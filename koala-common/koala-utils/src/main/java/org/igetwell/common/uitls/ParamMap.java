package org.igetwell.common.uitls;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
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
    /**
     * 将url中参数封装成map
     * @param url
     * @return
     */
    public static Map<String, String> getUrlMap(String url) {
        Map<String, String> map = new HashMap<String, String>();
        if (CharacterUtils.isNotBlank(url)) {
            String[] arr = url.split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
     *
     * @param params
     *            待拼接的Map数据
     * @return 拼接好后的字符串
     */
    public static String coverMap2String(Map<String, String> params) {
        Set<String> keySet = params.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (!CharacterUtils.isBlank(params.get(k)) && params.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(params.get(k).trim()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
