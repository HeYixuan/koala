package org.springframework.cloud.hystrix.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Hystrix Headers 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("feign.context.headers")
public class KoalaHeadersProperties {

    /**
     * 用于 聚合层 向调用层传递用户信息 的请求头，默认：X-KOALA-TOKEN
     */
    private String headerName = "X-KOALA-TOKEN";

    /**
     * RestTemplate 和 Feign 透传到下层的 Headers 名称列表
     */
    private List<String> allowed = new ArrayList<>();
}
