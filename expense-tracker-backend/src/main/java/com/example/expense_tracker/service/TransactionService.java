package com.example.expense_tracker.service;

import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.TransactionCategory;
import com.example.expense_tracker.entity.TransactionType;
import com.example.expense_tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    // Define valid categories matching your TransactionCategory enum
    private static final Set<TransactionCategory> EXPENSE_CATEGORIES = EnumSet.of(
            TransactionCategory.FOOD,
            TransactionCategory.GROCERIES,
            TransactionCategory.RENT,
            TransactionCategory.UTILITIES,
            TransactionCategory.TRAVEL,
            TransactionCategory.HEALTH,
            TransactionCategory.ENTERTAINMENT,
            TransactionCategory.EDUCATION,
            TransactionCategory.SUBSCRIPTIONS,
            TransactionCategory.OTHER_EXPENSE
    );

    private static final Set<TransactionCategory> INCOME_CATEGORIES = EnumSet.of(
            TransactionCategory.SALARY,
            TransactionCategory.FREELANCE,
            TransactionCategory.BUSINESS,
            TransactionCategory.INVESTMENT,
            TransactionCategory.GIFTS,
            TransactionCategory.OTHER_INCOME
    );

    public List<Transaction> getAllTransaction(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Optional<Transaction> getTransactionByIdAndUserId(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId);
        return Optional.ofNullable(transaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        validateCategoryByType(transaction);
        return transactionRepository.save(transaction);
    }

    public Transaction editTransaction(Transaction transaction) {
        Optional<Transaction> existingTransaction = Optional.ofNullable(
                transactionRepository.findByIdAndUserId(transaction.getId(), transaction.getUser().getId()));

        if (existingTransaction.isPresent()) {
            validateCategoryByType(transaction);
            return transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found for this user.");
        }
    }

    public void deleteTransactionByIdAndUserId(Long transactionId, Long userId) {
        Optional<Transaction> transaction = getTransactionByIdAndUserId(transactionId, userId);
        transaction.ifPresent(transactionRepository::delete);
    }

    // Validate category against transaction type
    private void validateCategoryByType(Transaction transaction) {
        TransactionType type = transaction.getType();
        TransactionCategory category = transaction.getCategory();

        if (type == TransactionType.EXPENSE && !EXPENSE_CATEGORIES.contains(category)) {
            throw new IllegalArgumentException("Invalid category for EXPENSE: " + category);
        } else if (type == TransactionType.INCOME && !INCOME_CATEGORIES.contains(category)) {
            throw new IllegalArgumentException("Invalid category for INCOME: " + category);
        }
    }
}
