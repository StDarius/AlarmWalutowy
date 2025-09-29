
package com.example.datagatherer.service.impl;

import com.example.common.RateUpdateMessage;
import com.example.datagatherer.config.MessagingConfig;
import com.example.datagatherer.service.RatePublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "false", matchIfMissing = true)
public class RabbitRatePublisher implements RatePublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitRatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(RateUpdateMessage msg) {
        rabbitTemplate.convertAndSend(MessagingConfig.QUEUE_NAME, msg);
    }
}
