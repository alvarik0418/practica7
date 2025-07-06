package org.example.handler;

import org.example.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class LedgerRequestReplyClient {

    private final RabbitTemplate rabbitTemplate;

    public LedgerRequestReplyClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Transaction> sendTransaction(Transaction tx) {
        return Mono.fromCallable(() ->
                (Transaction) rabbitTemplate.convertSendAndReceive(
                        "ledger.exchange",
                        "ledger.entry.request",
                        tx
                )
        ).subscribeOn(Schedulers.boundedElastic());
    }
}
