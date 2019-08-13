package org.springframework.cloud.hystrix.context;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * HttpHeaders 获取器，用于跨服务和线程的传递
 */
public interface KoalaHttpHeadersGetter {

    /**
     * 获取 HttpHeaders
     *
     * @return HttpHeaders
     */
    @Nullable
    HttpHeaders get();
}
