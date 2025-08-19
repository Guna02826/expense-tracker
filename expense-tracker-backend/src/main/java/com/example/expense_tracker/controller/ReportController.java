package com.example.expense_tracker.controller;

import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.service.ReportService;
import com.example.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if(userOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long userId = userOptional.get().getId();
        Map<String, Double> summary = reportService.getSummary(userId);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/by-category")
    public ResponseEntity<Map<String, Double>> getByCategory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Long userId = userOptional.get().getId();
        Map<String, Double> categoryTotals = reportService.getTotalByCategory(userId);

        return ResponseEntity.ok(categoryTotals);
    }
}
