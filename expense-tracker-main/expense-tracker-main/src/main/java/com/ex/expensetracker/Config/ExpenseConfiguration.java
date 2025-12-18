package com.ex.expensetracker.Config;

import com.ex.expensetracker.Repository.ExpenseRepository;
import com.ex.expensetracker.model.Expense;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class ExpenseConfiguration {

    @Bean
    CommandLineRunner commandLineRunnerExpenses(ExpenseRepository expenseRepository){
        return args -> {
            if (expenseRepository.count() == 0) {

                Expense rent = new Expense(
                        "Rent",                      // name
                        null,                        // expenseId (auto-generated)
                        1L,                          // categoryId
                        200.0,                       // amount
                        LocalDate.now().minusDays(1),// creationDate
                        "Room rent",                 // comments
                        "admin"                      // ⭐ username (important)
                );

                expenseRepository.saveAll(List.of(rent));
            }
        };
    }
}
