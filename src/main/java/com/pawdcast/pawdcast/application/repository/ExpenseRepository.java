package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    
    // Find all expenses by user ID
    List<Expense> findByUserId(Integer userId);
    
    // Find expenses by user ID and date range
    List<Expense> findByUserIdAndExpenseDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);
    
    // Find expenses by user ID and category
    List<Expense> findByUserIdAndCategory(Integer userId, String category);
    
    // Find expenses by user ID and payment method
    List<Expense> findByUserIdAndPaymentMethod(Integer userId, String paymentMethod);
    
    // Find current month expenses for a user
    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE) AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE)")
    List<Expense> findCurrentMonthExpensesByUserId(@Param("userId") Integer userId);
    
    // Find expenses for a specific month and year
    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    List<Expense> findExpensesByUserIdAndMonthYear(@Param("userId") Integer userId, 
                                                   @Param("month") int month, 
                                                   @Param("year") int year);
    
    // Get total amount spent by user in a date range
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByUserIdAndDateRange(@Param("userId") Integer userId, 
                                                 @Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);
    
    // Get expenses ordered by date (newest first)
    List<Expense> findByUserIdOrderByExpenseDateDesc(Integer userId);
}