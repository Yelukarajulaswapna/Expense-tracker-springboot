package com.ex.expensetracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Schema(description = "Name of the item", example = "Shoes", required = true)
    @NotEmpty(message = "Expense name cannot be empty")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID is auto-generated", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long expenseId;

    @Schema(description = "Category ID, use category endpoints to find out suitable one", example = "1", required = true)
    private Long categoryId;

    @Min(1)
    @Schema(description = "Amount of the item", example = "70", required = true)
    private Double amount;

    @PastOrPresent
    @Schema(description = "Date of expense in YYYY-MM-DD format", example = "2021-07-31")
    private LocalDate creationDate;

    @Schema(description = "Additional information", example = "Nike running shoes")
    @Size(max = 500)
    private String comments;

    // in Expense.java
    @Schema(description = "Owner username", example = "swapna", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;


}