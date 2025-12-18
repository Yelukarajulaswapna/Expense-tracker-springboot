package com.ex.expensetracker.Config;

import com.ex.expensetracker.Repository.CategoryRepository;
import com.ex.expensetracker.model.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CategoryConfiguration {

    @Bean
    CommandLineRunner commandLineRunnerCategories(CategoryRepository categoryRepository) {
        return args -> {

            String systemUser = "system"; // 👈 VERY IMPORTANT

            if (categoryRepository.count() == 0) {
                List<Category> defaultCategories = List.of(
                        new Category(null, "others", "Default category", systemUser),
                        new Category(null, "groceries", "You can add groceries here", systemUser),
                        new Category(null, "travel", "You can add travel here", systemUser),
                        new Category(null, "concerts", "You can add concerts here", systemUser),
                        new Category(null, "cinema", "You can add cinema here", systemUser),
                        new Category(null, "fitness", "You can add fitness here", systemUser)
                );

                categoryRepository.saveAll(defaultCategories);
            }
        };
    }
}