package com.example.expense_tracker;

import com.example.expense_tracker.entity.Transaction;
import com.example.expense_tracker.entity.TransactionCategory;
import com.example.expense_tracker.entity.TransactionType;
import com.example.expense_tracker.entity.User;
import com.example.expense_tracker.repository.TransactionRepository;
import com.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("demo@trackeroo.com").isPresent()) {
            System.out.println("✅ Demo user already exists, skipping seed.");
            return;
        }

        System.out.println("🌱 Seeding demo user and transactions...");

        // 1. Create demo user
        User demoUser = User.builder()
                .username("Demo User")
                .email("demo@trackeroo.com")
                .password(passwordEncoder.encode("demo123"))
                .build();
        demoUser = userRepository.save(demoUser);

        // 2. Create all transactions
        List<Transaction> transactions = List.of(
            // ===== INCOME (12 entries) =====
            tx(demoUser, "March Salary", 65000, TransactionType.INCOME, TransactionCategory.SALARY, LocalDate.of(2025, 3, 1)),
            tx(demoUser, "Freelance Logo Design", 8000, TransactionType.INCOME, TransactionCategory.FREELANCE, LocalDate.of(2025, 3, 15)),
            tx(demoUser, "Stock Dividend - Infosys", 3200, TransactionType.INCOME, TransactionCategory.INVESTMENT, LocalDate.of(2025, 3, 22)),
            tx(demoUser, "April Salary", 65000, TransactionType.INCOME, TransactionCategory.SALARY, LocalDate.of(2025, 4, 1)),
            tx(demoUser, "Freelance Website Redesign", 12000, TransactionType.INCOME, TransactionCategory.FREELANCE, LocalDate.of(2025, 4, 10)),
            tx(demoUser, "Birthday Gift from Family", 5000, TransactionType.INCOME, TransactionCategory.GIFTS, LocalDate.of(2025, 4, 18)),
            tx(demoUser, "Sold Old Laptop", 7500, TransactionType.INCOME, TransactionCategory.OTHER_INCOME, LocalDate.of(2025, 4, 25)),
            tx(demoUser, "May Salary", 65000, TransactionType.INCOME, TransactionCategory.SALARY, LocalDate.of(2025, 5, 1)),
            tx(demoUser, "Freelance API Integration", 15000, TransactionType.INCOME, TransactionCategory.FREELANCE, LocalDate.of(2025, 5, 5)),
            tx(demoUser, "Mutual Fund Returns", 4800, TransactionType.INCOME, TransactionCategory.INVESTMENT, LocalDate.of(2025, 5, 12)),
            tx(demoUser, "Business Consulting Fee", 10000, TransactionType.INCOME, TransactionCategory.BUSINESS, LocalDate.of(2025, 5, 18)),
            tx(demoUser, "Cashback Reward", 1200, TransactionType.INCOME, TransactionCategory.OTHER_INCOME, LocalDate.of(2025, 5, 20)),

            // ===== EXPENSES (33 entries) =====
            tx(demoUser, "Dominos Pizza Night", 850, TransactionType.EXPENSE, TransactionCategory.FOOD, LocalDate.of(2025, 3, 2)),
            tx(demoUser, "Weekly Groceries - BigBasket", 2400, TransactionType.EXPENSE, TransactionCategory.GROCERIES, LocalDate.of(2025, 3, 5)),
            tx(demoUser, "March House Rent", 15000, TransactionType.EXPENSE, TransactionCategory.RENT, LocalDate.of(2025, 3, 6)),
            tx(demoUser, "Electricity Bill - March", 1800, TransactionType.EXPENSE, TransactionCategory.UTILITIES, LocalDate.of(2025, 3, 8)),
            tx(demoUser, "Uber to Office - Week 1", 450, TransactionType.EXPENSE, TransactionCategory.TRAVEL, LocalDate.of(2025, 3, 10)),
            tx(demoUser, "Doctor Consultation", 700, TransactionType.EXPENSE, TransactionCategory.HEALTH, LocalDate.of(2025, 3, 14)),
            tx(demoUser, "Netflix Subscription", 649, TransactionType.EXPENSE, TransactionCategory.SUBSCRIPTIONS, LocalDate.of(2025, 3, 16)),
            tx(demoUser, "Movie Tickets - Dune 2", 600, TransactionType.EXPENSE, TransactionCategory.ENTERTAINMENT, LocalDate.of(2025, 3, 18)),
            tx(demoUser, "Udemy Course - React Advanced", 499, TransactionType.EXPENSE, TransactionCategory.EDUCATION, LocalDate.of(2025, 3, 20)),
            tx(demoUser, "Swiggy Dinner Order", 520, TransactionType.EXPENSE, TransactionCategory.FOOD, LocalDate.of(2025, 3, 25)),
            tx(demoUser, "Grocery Restock - DMart", 1800, TransactionType.EXPENSE, TransactionCategory.GROCERIES, LocalDate.of(2025, 3, 28)),
            tx(demoUser, "Biryani at Paradise", 380, TransactionType.EXPENSE, TransactionCategory.FOOD, LocalDate.of(2025, 4, 2)),
            tx(demoUser, "April House Rent", 15000, TransactionType.EXPENSE, TransactionCategory.RENT, LocalDate.of(2025, 4, 4)),
            tx(demoUser, "Internet Bill - April", 999, TransactionType.EXPENSE, TransactionCategory.UTILITIES, LocalDate.of(2025, 4, 6)),
            tx(demoUser, "Train Ticket to Hyderabad", 1200, TransactionType.EXPENSE, TransactionCategory.TRAVEL, LocalDate.of(2025, 4, 8)),
            tx(demoUser, "Gym Membership Renewal", 2500, TransactionType.EXPENSE, TransactionCategory.HEALTH, LocalDate.of(2025, 4, 10)),
            tx(demoUser, "Spotify + YouTube Premium", 349, TransactionType.EXPENSE, TransactionCategory.SUBSCRIPTIONS, LocalDate.of(2025, 4, 12)),
            tx(demoUser, "Concert Tickets - Coldplay", 4500, TransactionType.EXPENSE, TransactionCategory.ENTERTAINMENT, LocalDate.of(2025, 4, 14)),
            tx(demoUser, "Coursera Subscription", 3199, TransactionType.EXPENSE, TransactionCategory.EDUCATION, LocalDate.of(2025, 4, 16)),
            tx(demoUser, "Weekly Groceries - Zepto", 2100, TransactionType.EXPENSE, TransactionCategory.GROCERIES, LocalDate.of(2025, 4, 19)),
            tx(demoUser, "Cafe Coffee Day - Team Outing", 1100, TransactionType.EXPENSE, TransactionCategory.FOOD, LocalDate.of(2025, 4, 22)),
            tx(demoUser, "Water Bill - April", 350, TransactionType.EXPENSE, TransactionCategory.UTILITIES, LocalDate.of(2025, 4, 26)),
            tx(demoUser, "Auto Repair - Tyre Change", 3800, TransactionType.EXPENSE, TransactionCategory.OTHER_EXPENSE, LocalDate.of(2025, 4, 28)),
            tx(demoUser, "Zomato Weekend Binge", 760, TransactionType.EXPENSE, TransactionCategory.FOOD, LocalDate.of(2025, 5, 2)),
            tx(demoUser, "May House Rent", 15000, TransactionType.EXPENSE, TransactionCategory.RENT, LocalDate.of(2025, 5, 4)),
            tx(demoUser, "Groceries - BigBasket", 2600, TransactionType.EXPENSE, TransactionCategory.GROCERIES, LocalDate.of(2025, 5, 6)),
            tx(demoUser, "Gas Bill - May", 650, TransactionType.EXPENSE, TransactionCategory.UTILITIES, LocalDate.of(2025, 5, 8)),
            tx(demoUser, "Flight to Goa", 5400, TransactionType.EXPENSE, TransactionCategory.TRAVEL, LocalDate.of(2025, 5, 10)),
            tx(demoUser, "Pharmacy - Vitamins", 1200, TransactionType.EXPENSE, TransactionCategory.HEALTH, LocalDate.of(2025, 5, 13)),
            tx(demoUser, "AWS Subscription", 2100, TransactionType.EXPENSE, TransactionCategory.SUBSCRIPTIONS, LocalDate.of(2025, 5, 15)),
            tx(demoUser, "Bowling + Arcade Night", 1500, TransactionType.EXPENSE, TransactionCategory.ENTERTAINMENT, LocalDate.of(2025, 5, 17)),
            tx(demoUser, "New Headphones", 3499, TransactionType.EXPENSE, TransactionCategory.OTHER_EXPENSE, LocalDate.of(2025, 5, 19)),
            tx(demoUser, "LinkedIn Learning Annual", 7999, TransactionType.EXPENSE, TransactionCategory.EDUCATION, LocalDate.of(2025, 5, 22))
        );

        transactionRepository.saveAll(transactions);
        System.out.println("✅ Demo user seeded with " + transactions.size() + " transactions.");
    }

    /**
     * Helper method to build a Transaction object.
     */
    private Transaction tx(User user, String title, double amount, TransactionType type, TransactionCategory category, LocalDate date) {
        return Transaction.builder()
                .user(user)
                .title(title)
                .amount(amount)
                .type(type)
                .category(category)
                .date(date)
                .build();
    }
}
