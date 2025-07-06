package org.example.handler;

import org.example.dto.TransactionDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @RabbitListener(queues = "transaction.created.queue")
    public void handle(TransactionDto tx){
        System.out.println("Evento recibido "+tx);
    }
}
