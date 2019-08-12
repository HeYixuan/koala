package org.igetwell.common.listener;

import org.igetwell.common.event.ErrorEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class ErrorEventListener {

    /**
     * 建议使用 异步
     * @param event MicaErrorEvent
     */
    @Async
    @EventListener(ErrorEvent.class)
    public void handleError(ErrorEvent event) {
    }
}
