package com.example.expense_tracker.service;

import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TransactionService transactionService;

    public Map<String, BigDecimal> getSummary(Long userId) {
        List<Transaction> transactions = transactionService.getAllTransaction(userId);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome = totalIncome.add(t.getAmount());
            } else if (t.getType() == TransactionType.EXPENSE) {
                totalExpense = totalExpense.add(t.getAmount());
            }
        }

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("netBalance", netBalance);

        return summary;
    }

    public Map<String, BigDecimal> getTotalByCategory(Long userId) {
        List<Transaction> transactions = transactionService.getAllTransaction(userId);

        Map<String, BigDecimal> categoryTotals = new HashMap<>();

        for (Transaction t : transactions) {
            String category = t.getCategory().name();
            categoryTotals.merge(category, t.getAmount(), BigDecimal::add);
        }

        return categoryTotals;
    }
}
