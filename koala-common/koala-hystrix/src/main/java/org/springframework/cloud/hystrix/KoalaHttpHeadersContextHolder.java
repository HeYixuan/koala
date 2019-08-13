package org.springframework.cloud.hystrix;

import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * HttpHeadersContext
 */
public class KoalaHttpHeadersContextHolder {

    private static final ThreadLocal<HttpHeaders> HTTP_HEADERS_HOLDER = new NamedThreadLocal<>("Koala hystrix HttpHeaders");

    static void set(HttpHeaders httpHeaders) {
        HTTP_HEADERS_HOLDER.set(httpHeaders);
    }

    @Nullable
    public static HttpHeaders get() {
        return HTTP_HEADERS_HOLDER.get();
    }

    static void remove() {
        HTTP_HEADERS_HOLDER.remove();
    }

}
