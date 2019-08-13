package org.springframework.cloud.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.hystrix.KoalaHttpHeadersContextHolder;
import org.springframework.http.HttpHeaders;

/**
 * feign 传递Request header
 */
public class KoalaFeignRequestHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 默认都使用 hystrix
        HttpHeaders headers = KoalaHttpHeadersContextHolder.get();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, values) ->
                    values.forEach(value -> requestTemplate.header(key, value))
            );
        }
    }
}
