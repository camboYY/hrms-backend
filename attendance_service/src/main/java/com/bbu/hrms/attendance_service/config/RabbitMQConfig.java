package com.bbu.hrms.attendance_service.config;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_LEAVE = "leave.exchange";
    public static final String ROUTING_LEAVE_APPROVED = "leave.approved";
    public static final String QUEUE_ATTENDANCE_LEAVE_APPROVED = "leave.approved.queue";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public TopicExchange leaveExchange() {
        return new TopicExchange(EXCHANGE_LEAVE);
    }

    @Bean
    public Queue leaveApprovedQueue() {
        return new Queue(QUEUE_ATTENDANCE_LEAVE_APPROVED, true);
    }

    @Bean
    public Binding leaveApprovedBinding(Queue leaveApprovedQueue, TopicExchange leaveExchange) {
        return BindingBuilder.bind(leaveApprovedQueue)
                .to(leaveExchange)
                .with(ROUTING_LEAVE_APPROVED);
    }
}
