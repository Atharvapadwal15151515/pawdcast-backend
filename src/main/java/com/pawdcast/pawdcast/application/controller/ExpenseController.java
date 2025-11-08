package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Expense;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.ExpenseService;
import com.pawdcast.pawdcast.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private AuthService authService;
    
    private User getCurrentUser(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not logged in");
        }
        return authService.findByEmail(userEmail);
    }
    
    @PostMapping
    public ResponseEntity<?> createExpense(
            @RequestBody Expense expense,
            HttpServletRequest request) {
        
        User user = getCurrentUser(request);
        System.out.println("=== CREATE EXPENSE ===");
        System.out.println("User: " + user);
        
        if (user == null) {
            System.out.println("ERROR: User not found");
            return ResponseEntity.status(401).body("User not logged in");
        }
        System.out.println("User ID: " + user.getId());
        System.out.println("User Email: " + user.getEmail());

        expense.setUserId(user.getId());
        System.out.println("Expense user ID set to: " + expense.getUserId());
        
        try {
            Expense savedExpense = expenseService.createExpense(expense);
            System.out.println("Expense saved successfully with ID: " + savedExpense.getExpenseId());
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            System.out.println("Error saving expense: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating expense: " + e.getMessage());
        }
    }
    
    // Get all expenses for current user
    @GetMapping
    public ResponseEntity<?> getAllExpenses(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<Expense> expenses = expenseService.getAllExpensesByUserId(user.getId());
        return ResponseEntity.ok(expenses);
    }
    
    // Get current month expenses
    @GetMapping("/current-month")
    public ResponseEntity<?> getCurrentMonthExpenses(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<Expense> expenses = expenseService.getCurrentMonthExpenses(user.getId());
        return ResponseEntity.ok(expenses);
    }
    
    // Get expense by ID
    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(@PathVariable Integer expenseId, HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        Optional<Expense> expense = expenseService.getExpenseByIdAndUserId(expenseId, user.getId());
        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get expenses by date range
    @GetMapping("/date-range")
    public ResponseEntity<?> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<Expense> expenses = expenseService.getExpensesByDateRange(user.getId(), startDate, endDate);
        return ResponseEntity.ok(expenses);
    }
    
    // Get expenses by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getExpensesByCategory(@PathVariable String category, HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<Expense> expenses = expenseService.getExpensesByCategory(user.getId(), category);
        return ResponseEntity.ok(expenses);
    }
    
    // Get total expenses for date range
    @GetMapping("/total")
    public ResponseEntity<?> getTotalExpenses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        BigDecimal total = expenseService.getTotalExpenses(user.getId(), startDate, endDate);
        return ResponseEntity.ok(total);
    }
    
    // Update an expense
    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Integer expenseId,
            @RequestBody Expense expenseDetails,
            HttpServletRequest request) {
        
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        Expense updatedExpense = expenseService.updateExpense(expenseId, user.getId(), expenseDetails);
        if (updatedExpense != null) {
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Delete an expense
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Integer expenseId, HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        boolean deleted = expenseService.deleteExpense(expenseId, user.getId());
        if (deleted) {
            return ResponseEntity.ok("Expense deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get all categories for current user
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<String> categories = expenseService.getDistinctCategoriesByUserId(user.getId());
        return ResponseEntity.ok(categories);
    }
    
    // Get expenses by month and year
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyExpenses(
            @RequestParam int month,
            @RequestParam int year,
            HttpServletRequest request) {
        
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        List<Expense> expenses = expenseService.getExpensesByMonthYear(user.getId(), month, year);
        return ResponseEntity.ok(expenses);
    }
    
    // Session debug endpoint (updated for JWT)
    @GetMapping("/session-debug")
    public ResponseEntity<?> sessionDebug(HttpServletRequest request) {
        User user = getCurrentUser(request);
        
        java.util.Map<String, Object> debugInfo = new java.util.HashMap<>();
        debugInfo.put("userInRequest", user);
        
        if (user != null) {
            debugInfo.put("userId", user.getId());
            debugInfo.put("userEmail", user.getEmail());
        }
        
        // JWT specific info
        String userEmail = (String) request.getAttribute("userEmail");
        debugInfo.put("jwtUserEmail", userEmail);
        
        return ResponseEntity.ok(debugInfo);
    }

    // Get dashboard statistics
    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        try {
            Map<String, Object> stats = expenseService.getDashboardStats(user.getId());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.out.println("Error getting dashboard stats: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting dashboard statistics");
        }
    }

    @GetMapping("/current-month-stats")
    public ResponseEntity<?> getCurrentMonthStats(HttpServletRequest request) {
        User user = getCurrentUser(request);
        if (user == null) {
            return ResponseEntity.status(401).body("User not logged in");
        }
        
        try {
            Map<String, Object> stats = expenseService.getCurrentMonthStats(user.getId());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.out.println("Error getting current month stats: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error getting current month statistics");
        }
    }
}