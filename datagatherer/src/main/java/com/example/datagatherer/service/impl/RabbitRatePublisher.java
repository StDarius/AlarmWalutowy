package com.example.datagatherer.service.impl;

import com.example.common.RateUpdateMessage;
import com.example.common.messaging.MQ;
import com.example.datagatherer.service.RatePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.messaging.inMemory", havingValue = "false", matchIfMissing = true)
public class RabbitRatePublisher implements RatePublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitRatePublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitRatePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(RateUpdateMessage msg) {
        try {
            log.info("Publishing to {}:{} -> {}", MQ.EXCHANGE, MQ.ROUTING_KEY, msg);
            rabbitTemplate.convertAndSend(MQ.EXCHANGE, MQ.ROUTING_KEY, msg);
        } catch (AmqpException ex) {
            log.warn("RabbitMQ publish failed: {}", ex.getMessage(), ex);
        }
    }
}
