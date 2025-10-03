package com.example.dataprovider.config;

import com.example.common.messaging.MQ;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }

    @Bean
    public Queue ratesQueue() {
        return QueueBuilder.durable(MQ.QUEUE).build();
    }

    @Bean
    public DirectExchange ratesExchange() {
        return new DirectExchange(MQ.EXCHANGE, true, false);
    }

    @Bean
    public Binding ratesBinding(Queue ratesQueue, DirectExchange ratesExchange) {
        return BindingBuilder.bind(ratesQueue).to(ratesExchange).with(MQ.ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(converter);
        f.setMissingQueuesFatal(false);
        f.setDefaultRequeueRejected(false);
        return f;
    }
}
