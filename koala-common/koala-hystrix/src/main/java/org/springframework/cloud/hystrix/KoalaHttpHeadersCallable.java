package org.springframework.cloud.hystrix;

import org.springframework.cloud.hystrix.context.KoalaHttpHeadersGetter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

/**
 * HttpHeaders hystrix Callable
 */
public class KoalaHttpHeadersCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    @Nullable
    private HttpHeaders httpHeaders;

    public KoalaHttpHeadersCallable(Callable<V> delegate, @Nullable KoalaHttpHeadersGetter headersGetter) {
        this.delegate = delegate;
        this.httpHeaders = headersGetter == null ? null : headersGetter.get();
    }

    @Override
    public V call() throws Exception {
        if (httpHeaders == null) {
            return delegate.call();
        }
        try {
            KoalaHttpHeadersContextHolder.set(httpHeaders);
            return delegate.call();
        } finally {
            KoalaHttpHeadersContextHolder.remove();
            httpHeaders.clear();
            httpHeaders = null;
        }
    }
}
