package com.nicolas.expenses_tracker.dto.expense;

import com.nicolas.expenses_tracker.model.expense.Expense;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDTO {

    private Long id;

    @NotNull(message = "Expense must contain a value greater than 0")
    @Positive(message = "Expense must be greater than 0")
    private Float value;

    @NotNull(message = "Category must not be null")
    private String category;

    @NotNull
    private LocalDateTime createdAt;

    public static Expense toExpense(ExpenseDTO expenseDTO) {
        return Expense.builder()
                .value(expenseDTO.getValue())
                .category(expenseDTO.getCategory())
                .createdAt(expenseDTO.getCreatedAt())
                .build();
    }

    public static ExpenseDTO toDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .value(expense.getValue())
                .category(expense.getCategory())
                .createdAt(expense.getCreatedAt())
                .build();
    }



}
