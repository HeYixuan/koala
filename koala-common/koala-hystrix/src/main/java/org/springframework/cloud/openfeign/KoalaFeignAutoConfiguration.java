package org.springframework.cloud.openfeign;

import com.netflix.hystrix.HystrixCommand;
import feign.Contract;
import feign.Feign;
import feign.RequestInterceptor;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;


/**
 * feign 增强配置
 *
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(KoalaFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class KoalaFeignAutoConfiguration {

    @Configuration("hystrixFeignConfiguration")
    @ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
    protected static class HystrixFeignConfiguration {
        @Bean
        @Primary
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        @ConditionalOnProperty(value = "feign.hystrix.enabled", matchIfMissing = true)
        public Feign.Builder feignHystrixBuilder(
                RequestInterceptor requestInterceptor, Contract feignContract) {
            return HystrixFeign.builder()
                    .contract(feignContract)
                    .decode404()
                    .requestInterceptor(requestInterceptor);
        }

        @Bean
        @ConditionalOnMissingBean
        public RequestInterceptor requestInterceptor() {
            return new KoalaFeignRequestHeaderInterceptor();
        }
    }

    /**
     * mica enum 《-》 String 转换配置
     *
     * @param objectProvider ObjectProvider
     * @return SpringMvcContract
    @Bean
    public Contract feignContract(@Qualifier("mvcConversionService") ObjectProvider<ConversionService> objectProvider) {
        ConversionService conversionService = objectProvider.getIfAvailable(DefaultConversionService::new);
        ConverterRegistry converterRegistry = ((ConverterRegistry) conversionService);
        converterRegistry.addConverter(Enu()new EnumToStringConverter());
        converterRegistry.addConverter(new StringToEnumConverter());
        return new MicaSpringMvcContract(new ArrayList<>(), conversionService);
    }*/
}
