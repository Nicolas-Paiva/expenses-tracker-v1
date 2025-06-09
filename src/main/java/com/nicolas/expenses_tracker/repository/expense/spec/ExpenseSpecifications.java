package com.nicolas.expenses_tracker.repository.expense.spec;

import com.nicolas.expenses_tracker.model.expense.Expense;
import com.nicolas.expenses_tracker.model.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecifications {

    public static Specification<Expense> withFilters(User user,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     String category,
                                                     Float minValue,
                                                     Float maxValue) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Required: User
            predicates.add(criteriaBuilder.equal(root.get("user"), user));

            if (start != null && end != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), start, end));
            }

            if (category != null && !category.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            if (minValue != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("value"), minValue));
            }

            if (maxValue != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("value"), maxValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
