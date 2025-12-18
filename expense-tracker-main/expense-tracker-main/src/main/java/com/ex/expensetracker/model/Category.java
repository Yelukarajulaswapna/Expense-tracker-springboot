package com.ex.expensetracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Class representing a category in the application")
public class Category {

    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    @Schema(description = "Unique identifier of the category",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the category",
            example = "Fitness",
            required = true)
    private String name;

    @Size(max = 500)
    @Schema(description = "Description of the category",
            example = "Fitness supplements",
            maxLength = 500)
    private String description;

    // ⭐ NEW FIELD (MOST IMPORTANT)
    @Schema(description = "Owner username of this category",
            example = "john")
    private String username;
}