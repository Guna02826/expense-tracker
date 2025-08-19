package com.example.expense_tracker.controller;

import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.service.TransactionService;
import com.example.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            List<Transaction> transactions = transactionService.getAllTransaction(userId);

            return ResponseEntity.ok(transactions);
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long userId = userOptional.get().getId();

        Optional<Transaction> transaction = transactionService.getTransactionByIdAndUserId(transactionId, userId);

        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transactionReq) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            transactionReq.setUser(user);

            Transaction savedTransaction = transactionService.saveTransaction(transactionReq);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction); // ✅ Return actual object
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody Transaction transactionReq) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        transactionReq.setId(transactionId);
        transactionReq.setUser(user);

        try {
            Transaction updated = transactionService.editTransaction(transactionReq);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Long userId = userOptional.get().getId();

        transactionService.deleteTransactionByIdAndUserId(transactionId, userId);

        return new ResponseEntity<>("Transaction deleted successfully (if existed)", HttpStatus.OK);
    }
}
