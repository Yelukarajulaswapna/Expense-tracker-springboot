package com.ex.expensetracker.Controller;

import com.ex.expensetracker.Exception.ExpenseExistsException;
import com.ex.expensetracker.Exception.ExpenseNotFoundException;
import com.ex.expensetracker.Service.ExpenseService;
import com.ex.expensetracker.model.Expense;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    // ---------- ABOUT ----------
    @GetMapping("/about")
    @Operation(summary = "General information on expense-tracker.")
    public String selfDescription() {
        return "Welcome to expense-tracking version 1.0.0. Secure multi-user version.";
    }

    // ---------- GET ALL (ONLY OWN) ----------
    @GetMapping
    @Operation(summary = "Get all expenses of logged-in user")
    public List<Expense> getAllExpenses() {
        return expenseService.getExpenses();
    }

    // ---------- GET BY ID ----------
    @GetMapping("/{expenseId}")
    @Operation(summary = "Get expense by ID (only if owner)")
    public Expense getExpenseById(@PathVariable Long expenseId) {
        try {
            return expenseService.getExpenseById(expenseId);
        } catch (ExpenseNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // ---------- CREATE ----------
    @PostMapping
    @Operation(summary = "Add a new expense")
    public Expense addExpense(@RequestBody Expense expense) {
        try {
            return expenseService.addExpense(expense);
        } catch (ExpenseExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid date format. Use YYYY-MM-DD", e);
        }
    }

    // ---------- UPDATE ----------
    @PutMapping("/{expenseId}")
    @Operation(summary = "Update expense (only if owner)")
    public Expense updateExpense(
            @PathVariable Long expenseId,
            @RequestBody Expense expense) {
        try {
            return expenseService.updateExpense(expenseId, expense).getBody();
        } catch (ExpenseNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{expenseId}")
    @Operation(summary = "Delete expense (only if owner)")
    public ResponseEntity<String> deleteExpense(
            @PathVariable Long expenseId) {
        try {
            expenseService.deleteExpenseById(expenseId);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (ExpenseNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}