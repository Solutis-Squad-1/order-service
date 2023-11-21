package br.com.solutis.squad1.orderservice.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ.
 * This class sets up the necessary configurations for connecting to RabbitMQ and message exchange.
 */
@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.queue.order.status}")
    private String orderStatusQueueName;

    @Value("${spring.rabbitmq.exchange.order.status}")
    private String orderStatusExchangeName;

    @Value("${spring.rabbitmq.routing-key.order.status}")
    private String orderStatusRoutingKey;

    @Bean
    public Queue orderStatusQueue() {
        return QueueBuilder
                .durable(orderStatusQueueName)
                .build();
    }

    @Bean
    DirectExchange orderStatusExchange() {
        return new DirectExchange(orderStatusExchangeName);
    }

    @Bean
    Binding orderStatusBinding() {
        return BindingBuilder
                .bind(orderStatusQueue())
                .to(orderStatusExchange())
                .with(orderStatusRoutingKey);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
