package com.example.datagatherer.config;

import com.example.common.messaging.MQ;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    /** Make sure topology exists even if dataprovider isn't up yet */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }

    /** MUST match the consumer side (Direct, durable) */
    @Bean
    public DirectExchange ratesExchange() {
        return new DirectExchange(MQ.EXCHANGE, true, false);
    }

    /** Same durable queue as the consumer listens to */
    @Bean
    public Queue ratesQueue() {
        return QueueBuilder.durable(MQ.QUEUE).build();
    }

    /** Bind queue to exchange with the shared routing key */
    @Bean
    public Binding ratesBinding(Queue ratesQueue, DirectExchange ratesExchange) {
        return BindingBuilder.bind(ratesQueue).to(ratesExchange).with(MQ.ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        tpl.setExchange(MQ.EXCHANGE);
        tpl.setRoutingKey(MQ.ROUTING_KEY);

        // Help diagnose routing issues (e.g., missing binding)
        tpl.setMandatory(true);
        tpl.setReturnsCallback(ret -> {
            System.err.printf(
                    "Rabbit RETURN: replyCode=%d replyText=%s exchange=%s routingKey=%s body=%s%n",
                    ret.getReplyCode(), ret.getReplyText(), ret.getExchange(), ret.getRoutingKey(),
                    new String(ret.getMessage().getBody())
            );
        });
        return tpl;
    }
}
