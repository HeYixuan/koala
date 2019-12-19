package org.igetwell.common.uitls;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.igetwell.common.handle.HttpClientFactory;
import org.igetwell.common.handle.JsonResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HttpClients {

    private static int timeout = 8000;

    private static int retryExecutionCount = 2;

    protected static CloseableHttpClient httpClient = HttpClientFactory.createHttpClient(100,10, timeout,retryExecutionCount);

    private static Map<String,CloseableHttpClient> MCH_KEY_STORE = new ConcurrentHashMap<String, CloseableHttpClient>();

    protected static final Header USER_AGENT = new BasicHeader(HttpHeaders.USER_AGENT,"koala-wechat-sdk v2.0");

    private static void setUserAgent(HttpUriRequest httpUriRequest){
        httpUriRequest.addHeader(USER_AGENT);
    }


    /**
     * 初始化   MCH HttpClient KeyStore
     * @param mchId mch_id
     * @param keyStoreFilePath keyStoreFilePath
     */
    public static void initMchKeyStore(String mchId,String keyStoreFilePath){
        try {
            initMchKeyStore(mchId, new FileInputStream(new File(keyStoreFilePath)));
        } catch (FileNotFoundException e) {
            log.error("init error", e);
        }
    }

    /**
     * 初始化   MCH HttpClient KeyStore
     * @since 2.8.7
     * @param mchId mchId
     * @param inputStream p12 文件流
     */
    public static void initMchKeyStore(String mchId, InputStream inputStream) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, mchId.toCharArray());
            inputStream.close();
            CloseableHttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mchId, timeout, retryExecutionCount);
            MCH_KEY_STORE.put(mchId, httpClient);
        } catch (Exception e) {
            log.error("init mch error", e);
        }
    }
    public static CloseableHttpResponse keyStoreExecute(String mchId, HttpUriRequest request){
        setUserAgent(request);
        try {
            return MCH_KEY_STORE.get(mchId).execute(request);
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }



    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler){
        setUserAgent(request);
        try {
            T t = httpClient.execute(request, responseHandler, HttpClientContext.create());
            return t;
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }


    /**
     * 数据返回自动JSON对象解析
     * @param request request
     * @param clazz clazz
     * @param <T> T
     * @return result
     */
    public static <T> T execute(HttpUriRequest request, Class<T> clazz){
        return execute(request, JsonResponseHandler.createResponseHandler(clazz));
    }

    public static <T> T keyStoreExecute(String mchId, HttpUriRequest request, ResponseHandler<T> responseHandler){
        try {
            T t = keyStoreExecute(mchId, request, responseHandler);
            return t;
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }

    public static CloseableHttpResponse execute(HttpUriRequest request){
        setUserAgent(request);
        try {
            return httpClient.execute(request, HttpClientContext.create());
        } catch (Exception e) {
            log.error("execute error", e);
        }
        return null;
    }
}
