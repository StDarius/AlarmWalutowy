
package com.example.datagatherer.service.impl;

import com.example.common.RateUpdateMessage;
import com.example.datagatherer.messaging.InMemoryEventBus;
import com.example.datagatherer.service.RatePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "true")
public class InMemoryRatePublisher implements RatePublisher {

    private final InMemoryEventBus bus;

    public InMemoryRatePublisher(InMemoryEventBus bus) {
        this.bus = bus;
    }

    @Override
    public void publish(RateUpdateMessage msg) {
        bus.publish(msg);
    }
}
