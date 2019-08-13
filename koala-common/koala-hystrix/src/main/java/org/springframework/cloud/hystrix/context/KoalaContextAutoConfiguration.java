package org.springframework.cloud.hystrix.context;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KoalaHeadersProperties.class})
public class KoalaContextAutoConfiguration {
}
