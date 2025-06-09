package com.nicolas.expenses_tracker.model.expense;

import com.nicolas.expenses_tracker.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents an expense that
 * is stored in the database.
 */
@Entity(name = "Expense")
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    // Allows PostgreSQL to
    // generate ID's for new users
    @SequenceGenerator(
            name = "expenses_seq", // Used by GeneratedValue
            sequenceName = "expenses_seq", // PostgreSQL sequence
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expenses_seq"
    )
    private Long id;

    @ManyToOne
    private User user;

    private float value;

    private String category;

    private LocalDateTime createdAt;
}
