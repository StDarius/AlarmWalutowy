
package com.example.dataprovider.messaging;

import com.example.common.RateUpdateMessage;
import com.example.dataprovider.service.RateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RateUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(RateUpdateConsumer.class);

    private final RateService rateService;

    public RateUpdateConsumer(RateService rateService) {
        this.rateService = rateService;
    }

    @ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "false", matchIfMissing = true)
    @RabbitListener(queues = "rate_updates")
    public void onMessage(RateUpdateMessage msg) {
        log.info("Received {}", msg);
        rateService.record(msg.getBase(), msg.getQuote(), msg.getRate(), msg.getChangePercent(), msg.getTimestamp());
    }

    @ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "true")
    @EventListener
    public void onEvent(RateUpdateMessage msg) {
        log.info("Received (in-memory) {}", msg);
        rateService.record(msg.getBase(), msg.getQuote(), msg.getRate(), msg.getChangePercent(), msg.getTimestamp());
    }
}
