package org.example.service;

import org.example.dto.CashRequestDto;
import org.example.dto.TransactionDto;
import org.example.handler.LedgerRequestReplyClient;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final LedgerClient ledger;
    private final LedgerRequestReplyClient legderRabbitClient;

    public TransactionService(TransactionRepository repo, LedgerClient ledger,LedgerRequestReplyClient legderRabbitClient) {
        this.repo = repo;
        this.ledger = ledger;
        //this.publisher = publisher;
        this.legderRabbitClient = legderRabbitClient;
    }

    public Mono<TransactionDto> cashIn(CashRequestDto req){
        Transaction tx = Transaction.builder()
                .amount(req.amount())
                .currency(req.currency())
                .type(TransactionType.CASH_IN)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        return repo.save(tx)
                .flatMap(legderRabbitClient::sendTransaction)
                .flatMap(ledger::postEntry)
                .map(this::toDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorResume(e->rollback(tx, e));
    }

    public Mono<TransactionDto> cashOut(CashRequestDto req){
        Transaction tx = Transaction.builder()
                .amount(req.amount())
                .currency(req.currency())
                .type(TransactionType.CASH_OUT)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        return repo.save(tx)
                .flatMap(legderRabbitClient::sendTransaction)
                .flatMap(ledger::postEntry)
                .map(this::toDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorResume(e->rollback(tx, e));
    }

    public Mono<TransactionDto> findById(String id){
        return repo.findById(id)
                .map(this::toDto);
                //.switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
    }

    private Mono<TransactionDto> rollback(Transaction tx, Throwable e) {
        tx.setStatus(TransactionStatus.FAILED);
        return repo.save(tx).then(Mono.error(e));
    }

    private TransactionDto toDto(Transaction transaction) {
        return  new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
}