package com.example.expense_tracker.controller;

import com.example.expense_tracker.dto.TransactionRequestDTO;
import com.example.expense_tracker.dto.TransactionResponseDTO;
import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.service.TransactionService;
import com.example.expense_tracker.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            List<Transaction> transactions = transactionService.getAllTransaction(userId);
            List<TransactionResponseDTO> response = transactions.stream()
                    .map(this::toResponseDTO)
                    .toList();
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long transactionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long userId = userOptional.get().getId();
        Optional<Transaction> transaction = transactionService.getTransactionByIdAndUserId(transactionId, userId);

        return transaction
                .map(t -> new ResponseEntity<>(toResponseDTO(t), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Transaction transaction = toEntity(dto);
            transaction.setUser(user);

            Transaction saved = transactionService.saveTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(saved));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequestDTO dto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        Transaction transaction = toEntity(dto);
        transaction.setId(transactionId);
        transaction.setUser(user);

        try {
            Transaction updated = transactionService.editTransaction(transaction);
            return new ResponseEntity<>(toResponseDTO(updated), HttpStatus.OK);
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

    // ── Mapper Methods ──

    private TransactionResponseDTO toResponseDTO(Transaction t) {
        return TransactionResponseDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .amount(t.getAmount())
                .type(t.getType())
                .category(t.getCategory())
                .date(t.getDate())
                .note(t.getNote())
                .build();
    }

    private Transaction toEntity(TransactionRequestDTO dto) {
        return Transaction.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .type(dto.getType())
                .category(dto.getCategory())
                .date(dto.getDate())
                .note(dto.getNote())
                .build();
    }
}
