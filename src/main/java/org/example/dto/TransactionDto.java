package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.model.TransactionType;
import org.example.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDto(String id,
                             @NotNull @Positive BigDecimal amount,
                             String currency,
                             TransactionType type,
                             TransactionStatus status,
                             Instant createdAt) {}
