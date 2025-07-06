package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document
public class Transaction {
    // campos: id, amount, currency, type, status, createdAt
    @Id
    private String id;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;

    private TransactionType type;
    private TransactionStatus status;
    private Instant createdAt;

    public Transaction(String id, BigDecimal amount, String currency, TransactionType type, TransactionStatus status, Instant createdAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{"+
                "id='" + id + '\''+
                ", amount='" + amount + '\''+
                ", currency='" + currency + '\''+
                ", type='" + type + '\''+
                ", status='" + status + '\''+
                ", createdAt='" + createdAt + '\''+
                "}";
    }
    public static class Builder{
        private String id;
        private BigDecimal amount;
        private String currency;
        private TransactionType type;
        private TransactionStatus status;
        private Instant createdAt;

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder amount(BigDecimal amount){
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency){
            this.currency = currency;
            return this;
        }

        public Builder type(TransactionType type){
            this.type = type;
            return this;
        }

        public Builder status(TransactionStatus status){
            this.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt){
            this.createdAt = createdAt;
            return this;
        }

        public Transaction build(){
            return new Transaction(id, amount, currency, type, status, createdAt);
        }
    }

    public static Builder builder(){
        return new Builder();
    }
}