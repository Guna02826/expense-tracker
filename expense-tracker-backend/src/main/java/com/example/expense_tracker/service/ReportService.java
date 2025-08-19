package com.example.expense_tracker.service;


import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TransactionService transactionService;

    public Map<String, Double> getSummary(Long userId){
        List<Transaction> transactions = transactionService.getAllTransaction(userId);

        double totalIncome = 0;
        double totalExpense = 0;

        for(Transaction t : transactions){
            if("income".equalsIgnoreCase(String.valueOf(t.getType()))){
                totalIncome += t.getAmount();
            } else if("expense".equalsIgnoreCase(String.valueOf(t.getType()))) {
                totalExpense += t.getAmount();
            }
        }

        double netBalance = totalIncome - totalExpense;

        Map<String, Double> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("netBalance", netBalance);

        return summary;
    }

    public Map<String, Double> getTotalByCategory(Long userId){
        List<Transaction> transactions = transactionService.getAllTransaction(userId);

        Map<String, Double> categoryTotals = new HashMap<>();

        for(Transaction t : transactions){
            String category = t.getCategory().name();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + t.getAmount());
        }

        return categoryTotals;
    }
}
