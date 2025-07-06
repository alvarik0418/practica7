package org.example.handler;

import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static org.example.config.AMQPConfig.EXCHANGE;
import static org.example.config.AMQPConfig.ROUTING_KEY;

@Component
public class Publisher {
    private final AmqpTemplate template;

    public Publisher(AmqpTemplate template) {
        this.template = template;
    }

    public void publishCreated(TransactionDto transaction){
        template.convertAndSend(EXCHANGE, ROUTING_KEY, transaction);
    }
}
