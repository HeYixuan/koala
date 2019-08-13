package org.springframework.cloud.hystrix;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.hystrix.context.KoalaHttpHeadersGetter;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;

/**
 * Hystrix 配置
 *
 */
@Configuration
@ConditionalOnClass(Hystrix.class)
public class KoalaHystrixAutoConfiguration {

    @Nullable
    @Autowired(required = false)
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;
    @Nullable
    @Autowired(required = false)
    private KoalaHttpHeadersGetter headersGetter;

    @PostConstruct
    public void init() {
        // Keeps references of existing Hystrix plugins.
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();

        HystrixPlugins.reset();

        // Registers existing plugins excepts the Concurrent Strategy plugin.
        HystrixConcurrencyStrategy strategy = new KoalaHystrixConcurrencyStrategy(existingConcurrencyStrategy, headersGetter);
        HystrixPlugins.getInstance().registerConcurrencyStrategy(strategy);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
    }
}
