package org.springframework.cloud.gateway.route.filter;

import lombok.extern.slf4j.Slf4j;
import org.igetwell.common.enums.SignType;
import org.igetwell.common.uitls.*;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.Part;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SignFilter extends AbstractGatewayFilterFactory {

    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            // 请求参数上的url地址
            Map<String, String> params = new HashMap<>();
            request.getQueryParams().forEach((key, items) -> {
                params.put(key, items.get(0));
            });
            // GET，直接向下执行
            if (HttpMethod.GET.name().equals(request.getMethodValue())) {
                return this.checkSign(params, chain, exchange);
            } else if (HttpMethod.POST.name().equals(request.getMethodValue())) {
                return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                    DataBufferUtils.retain(dataBuffer);
                    final Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                    final ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
                    final ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                    return cacheBody(mutatedExchange, chain, params);
                });

            }
            ServerHttpResponse response = exchange.getResponse();
            return response.setComplete();
        };
    }


    /***
     * 验证签名
     * @author Lance lance_lan_2016@163.com
     * @date 2020-01-07 09:57
     * @param params
     * @param chain
     * @param exchange
     * @return reactor.core.publisher.Mono<java.lang.Void>
     *
     * */
    private static Mono<Void> checkSign(Map<String, String> params, GatewayFilterChain chain, ServerWebExchange exchange) {
        try {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            if (!checkTimestamp(params) || !SignUtils.checkSign(params, "paterKey", SignType.MD5)) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(GsonUtils.toJson(ResponseEntity.error(org.igetwell.common.enums.HttpStatus.BAD_REQUEST, "验签失败")).getBytes())));
            }
        } catch (Exception e) {
            log.error("网关验证签名错误.", e);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(GsonUtils.toJson(ResponseEntity.error(org.igetwell.common.enums.HttpStatus.INTERNAL_SERVER_ERROR)).getBytes())));
        }
        return chain.filter(exchange);
    }

    private Mono<Void> cacheBody(ServerWebExchange exchange, GatewayFilterChain chain, Map<String, String> params) {
        final HttpHeaders headers = exchange.getRequest().getHeaders();
        if (headers.getContentLength() == 0) {
            return chain.filter(exchange);
        }
        final ResolvableType resolvableType;
        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())) {
            resolvableType = ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
        } else {
            resolvableType = ResolvableType.forClass(String.class);
        }

        return MESSAGE_READERS.stream().filter(reader -> reader.canRead(resolvableType,
                exchange.getRequest().getHeaders().getContentType())).findFirst()
                .orElseThrow(() -> new IllegalStateException("no suitable HttpMessageReader.")).readMono(resolvableType,
                        exchange.getRequest(), Collections.emptyMap()).flatMap(resolvedBody -> {
                    if (resolvedBody instanceof MultiValueMap) {
                        @SuppressWarnings("rawtypes")
                        MultiValueMap<String, Object> map = (MultiValueMap) resolvedBody;
                        map.keySet().forEach(key -> {
                            Object obj = map.get(key);
                            List<Object> list = (List<Object>) obj;
                            for (Object object : list) {
                                if (object.getClass().toString().equals("class org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader$SynchronossFilePart")) {
                                    continue;
                                }
                                Field[] fields = object.getClass().getDeclaredFields();
                                try {
                                    for (Field field : fields) {
                                        field.setAccessible(true);
                                        params.put(key, field.get(object).toString());
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        if (null != resolvedBody) {
                            String path = null;
                            try {
                                path = URLDecoder.decode(((Object) resolvedBody).toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            if (null != path) {
                                if (path.contains("&")) {
                                    String items[] = path.split("&");
                                    for (String item : items) {
                                        String subItems[] = item.split("=");
                                        if (null != subItems && subItems.length == 2) {
                                            params.put(subItems[0], subItems[1]);
                                        }
                                    }
                                } else {
                                    //body
                                    Map<String, String> bodyParams = GsonUtils.fromJson(path, Map.class);
                                    if (bodyParams != null) {
                                        for (Map.Entry entry : bodyParams.entrySet()) {
                                            params.put((String) entry.getKey(), String.valueOf(entry.getValue()));
                                        }
                                    }
                                }

                            }
                        }

                    }
                    return this.checkSign(params, chain, exchange);
                });
    }

    /**
     * 校验时间是不是超过10分钟
     * @param params
     * @return
     */
    private static boolean checkTimestamp(Map<String, String> params) {
        long timestamp = null == params.get("timestamp") ? 0 : DateUtils.getTimestamp() - Long.valueOf(params.get("timestamp"));
        long expire = 10 * 60 * 1000; //10分钟
        if (0 > timestamp || timestamp > expire) {
            return false;
        }
        return true;
    }
}
