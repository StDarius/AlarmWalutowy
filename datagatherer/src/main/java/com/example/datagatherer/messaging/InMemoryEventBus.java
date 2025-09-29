
package com.example.datagatherer.messaging;

import com.example.common.RateUpdateMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "true", matchIfMissing = false)
@Component
public class InMemoryEventBus {

    private final ApplicationEventPublisher publisher;

    public InMemoryEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(RateUpdateMessage msg) {
        publisher.publishEvent(msg);
    }
}
