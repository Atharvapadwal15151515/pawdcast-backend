package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer expenseId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(length = 100)
    private String category;
    
    // Constructors
    public Expense() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Expense(Integer userId, LocalDate expenseDate, String description, 
                  BigDecimal amount, String paymentMethod, String category) {
        this();
        this.userId = userId;
        this.expenseDate = expenseDate;
        this.description = description;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.category = category;
    }
    
    // Getters and Setters
    public Integer getExpenseId() {
        return expenseId;
    }
    
    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public LocalDate getExpenseDate() {
        return expenseDate;
    }
    
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", userId=" + userId +
                ", expenseDate=" + expenseDate +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}