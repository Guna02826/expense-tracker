package com.example.expense_tracker.dto;

import com.example.expense_tracker.entity.TransactionCategory;
import com.example.expense_tracker.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponseDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionCategory category;
    private LocalDate date;
    private String note;
}
