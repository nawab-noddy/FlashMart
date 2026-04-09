package com.inventory.FlashMart.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue(){
        return new Queue("order.queue");
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("order.exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("order.routing.key");
    }

    @Bean
    public MessageConverter messageConvertor(){
        return new JacksonJsonMessageConverter();
    }
}
