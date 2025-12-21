package com.ex.expensetracker.Repository;

import com.ex.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // ✅ Only categories of logged-in user
    List<Category> findByUsername(String username);

    // ✅ Find category by name + owner
    Optional<Category> findByNameAndUsername(String name, String username);

    // ✅ Find category by id + owner
    Optional<Category> findByIdAndUsername(Long id, String username);
}