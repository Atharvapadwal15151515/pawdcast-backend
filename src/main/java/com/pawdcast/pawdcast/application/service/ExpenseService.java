package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.Expense;
import com.pawdcast.pawdcast.application.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    // Create a new expense
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    
    // Get expense by ID and user ID (to ensure user can only access their own expenses)
    public Optional<Expense> getExpenseByIdAndUserId(Integer expenseId, Integer userId) {
        return expenseRepository.findById(expenseId)
                .filter(expense -> expense.getUserId().equals(userId));
    }
    
    // Get all expenses for a user
    public List<Expense> getAllExpensesByUserId(Integer userId) {
        return expenseRepository.findByUserIdOrderByExpenseDateDesc(userId);
    }
    
    // Get current month expenses for a user
    public List<Expense> getCurrentMonthExpenses(Integer userId) {
        return expenseRepository.findCurrentMonthExpensesByUserId(userId);
    }
    
    // Get expenses by date range
    public List<Expense> getExpensesByDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
    }
    
    // Get expenses by category
    public List<Expense> getExpensesByCategory(Integer userId, String category) {
        return expenseRepository.findByUserIdAndCategory(userId, category);
    }
    
    // Update an expense
    public Expense updateExpense(Integer expenseId, Integer userId, Expense expenseDetails) {
        Optional<Expense> optionalExpense = getExpenseByIdAndUserId(expenseId, userId);
        if (optionalExpense.isPresent()) {
            Expense existingExpense = optionalExpense.get();
            existingExpense.setExpenseDate(expenseDetails.getExpenseDate());
            existingExpense.setDescription(expenseDetails.getDescription());
            existingExpense.setAmount(expenseDetails.getAmount());
            existingExpense.setPaymentMethod(expenseDetails.getPaymentMethod());
            existingExpense.setCategory(expenseDetails.getCategory());
            return expenseRepository.save(existingExpense);
        }
        return null;
    }
    
    // Delete an expense
    public boolean deleteExpense(Integer expenseId, Integer userId) {
        Optional<Expense> optionalExpense = getExpenseByIdAndUserId(expenseId, userId);
        if (optionalExpense.isPresent()) {
            expenseRepository.delete(optionalExpense.get());
            return true;
        }
        return false;
    }
    
    // Get total expenses for a user in date range
    public BigDecimal getTotalExpenses(Integer userId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalAmountByUserIdAndDateRange(userId, startDate, endDate);
    }
    
    // Get expenses by month and year
    public List<Expense> getExpensesByMonthYear(Integer userId, int month, int year) {
        return expenseRepository.findExpensesByUserIdAndMonthYear(userId, month, year);
    }
    
    // Get all categories used by a user
    public List<String> getDistinctCategoriesByUserId(Integer userId) {
        return expenseRepository.findByUserId(userId).stream()
                .map(Expense::getCategory)
                .distinct()
                .toList();
    }
    public Map<String, Object> getCurrentMonthStats(Integer userId) {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        
        // Get current month expenses
        List<Expense> monthExpenses = expenseRepository.findByUserIdAndExpenseDateBetween(
            userId, firstDayOfMonth, lastDayOfMonth);
        
        // Calculate stats
        BigDecimal totalAmount = monthExpenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal largestExpense = monthExpenses.stream()
            .map(Expense::getAmount)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        int currentDay = now.getDayOfMonth();
        BigDecimal dailyAverage = currentDay > 0 ? 
            totalAmount.divide(BigDecimal.valueOf(currentDay), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalThisMonth", totalAmount);
        stats.put("dailyAverage", dailyAverage);
        stats.put("largestExpense", largestExpense);
        stats.put("expenseCount", monthExpenses.size());
        stats.put("monthExpenses", monthExpenses);
        
        return stats;
    }

    // Get dashboard statistics
    public Map<String, Object> getDashboardStats(Integer userId) {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        
        // Current month stats
        List<Expense> monthExpenses = expenseRepository.findByUserIdAndExpenseDateBetween(
            userId, firstDayOfMonth, lastDayOfMonth);
        
        BigDecimal totalThisMonth = monthExpenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal largestExpense = monthExpenses.stream()
            .map(Expense::getAmount)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        int currentDay = now.getDayOfMonth();
        BigDecimal dailyAverage = currentDay > 0 ? 
            totalThisMonth.divide(BigDecimal.valueOf(currentDay), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        // Category breakdown for current month
        Map<String, BigDecimal> categoryBreakdown = monthExpenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
            ));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalThisMonth", totalThisMonth);
        stats.put("dailyAverage", dailyAverage);
        stats.put("largestExpense", largestExpense);
        stats.put("expenseCount", monthExpenses.size());
        stats.put("categoryBreakdown", categoryBreakdown);
        
        return stats;
    }
}