package com.ex.expensetracker.Repository;

import com.ex.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // get all expenses for one user
    List<Expense> findByUsername(String username);

    // find by name within a single user (used to check duplicates)
    Optional<Expense> findByNameAndUsername(String name, String username);

    // single expense by id + owner (fast ownership check)

    Optional<Expense> findByExpenseIdAndUsername(Long expenseId, String username);

    // if you still use the older method name, keep it but prefer the above ones
    Optional<Expense> findExpenseByName(String name); // optional — but beware it was global
}