package com.ex.expensetracker.Service;

import com.ex.expensetracker.Exception.ExpenseExistsException;
import com.ex.expensetracker.Exception.ExpenseNotFoundException;
import com.ex.expensetracker.Repository.ExpenseRepository;
import com.ex.expensetracker.model.Expense;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // 🔐 current logged-in user
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : auth.getName();
    }

    // ---------- READ ALL ----------
    public List<Expense> getExpenses() {
        String username = getCurrentUsername();
        if (username == null) return List.of();
        return expenseRepository.findByUsername(username);
    }

    // ---------- READ BY ID ----------
    public Expense getExpenseById(Long expenseId) throws ExpenseNotFoundException {
        String username = getCurrentUsername();
        if (username == null) {
            throw new ExpenseNotFoundException("Expense not found");
        }

        return expenseRepository
                .findByExpenseIdAndUsername(expenseId, username)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense does not exist"));
    }

    // ---------- READ BY NAME ----------
    public Expense getExpenseByName(String name) throws ExpenseNotFoundException {
        String username = getCurrentUsername();
        if (username == null) {
            throw new ExpenseNotFoundException("Expense not found");
        }

        return expenseRepository
                .findByNameAndUsername(name, username)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense does not exist"));
    }

    // ---------- CREATE ----------
    public Expense addExpense(Expense expense) throws ExpenseExistsException {
        String username = getCurrentUsername();
        if (username == null) {
            throw new ExpenseExistsException("Not authenticated");
        }

        Optional<Expense> existing =
                expenseRepository.findByNameAndUsername(expense.getName(), username);

        if (existing.isPresent()) {
            throw new ExpenseExistsException(
                    "Expense with that name already exists for this user");
        }

        expense.setUsername(username);
        return expenseRepository.save(expense);
    }

    // ---------- DELETE ----------
    public void deleteExpenseById(Long expenseId) throws ExpenseNotFoundException {
        String username = getCurrentUsername();
        if (username == null) {
            throw new ExpenseNotFoundException("Expense not found");
        }

        Expense expense = expenseRepository
                .findByExpenseIdAndUsername(expenseId, username)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense does not exist"));

        log.info("Deleting expense {} for user {}", expenseId, username);
        expenseRepository.delete(expense);
    }

    // ---------- UPDATE ----------
    @Transactional
    public ResponseEntity<Expense> updateExpense(Long expenseId, Expense newExpense)
            throws ExpenseNotFoundException {

        String username = getCurrentUsername();
        if (username == null) {
            throw new ExpenseNotFoundException("Expense not found");
        }

        Expense existing = expenseRepository
                .findByExpenseIdAndUsername(expenseId, username)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense does not exist"));

        existing.setName(newExpense.getName());
        existing.setAmount(newExpense.getAmount());
        existing.setCategoryId(newExpense.getCategoryId());
        existing.setComments(newExpense.getComments());
        existing.setCreationDate(newExpense.getCreationDate());

        return ResponseEntity.ok(expenseRepository.save(existing));
    }
}