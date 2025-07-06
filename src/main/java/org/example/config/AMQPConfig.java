package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    public static final String EXCHANGE = "ledger.exchange";
    public static final String ROUTING_KEY = "ledger.entry.request";

    @Bean
    public DirectExchange ledgerExchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue requestQueue(){
        return new Queue("ledger.entry.request.queue", true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter jsonMessageConverter){
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(jsonMessageConverter);
        template.setReplyTimeout(5000);
        template.setUseTemporaryReplyQueues(true);
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory cf){
        return new RabbitAdmin(cf);
    }
}
