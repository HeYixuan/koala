package org.igetwell.wechat.sdk.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClients;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MchPayAPI extends API {


    /**
     * 统一下单
     */
    public static String execute(String uri, Map<String, String> params) throws IOException {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            list.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setUri(uri)
                .setEntity(urlEncodedFormEntity)
                .build();
        CloseableHttpResponse response = HttpClients.execute(httpUriRequest);
        String str = EntityUtils.toString(response.getEntity(),"UTF-8");
        return str;
    }

    /**
     * 统一下单
     */
    public static String pushOrder(String xml) throws IOException {
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_XML)
                .setUri(MCH_URI+ "/pay/unifiedorder")
                .setEntity(new StringEntity(xml, Charset.forName("UTF-8")))
                .build();
        CloseableHttpResponse response = HttpClients.execute(httpUriRequest);
        String str = EntityUtils.toString(response.getEntity(),"UTF-8");
        return str;
    }

    /**
     * 申请退款
     */
    public static String refund(String mchId, String keyStoreFilePath, String xml) throws IOException {
        HttpClients.initMchKeyStore(mchId, keyStoreFilePath);
        HttpUriRequest httpUriRequest = RequestBuilder
                .post()
                .setHeader(APPLICATION_XML)
                .setUri(MCH_URI+ "/secapi/pay/refund")
                .setEntity(new StringEntity(xml, Charset.forName("UTF-8")))
                .build();
        CloseableHttpResponse response = HttpClients.keyStoreExecute(mchId, httpUriRequest);
        String str = EntityUtils.toString(response.getEntity(),"UTF-8");
        return str;
    }

}
