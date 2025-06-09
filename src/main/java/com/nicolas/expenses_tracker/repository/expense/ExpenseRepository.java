package com.nicolas.expenses_tracker.repository.expense;


import com.nicolas.expenses_tracker.model.expense.Expense;
import com.nicolas.expenses_tracker.model.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    List<Expense> findByUser(User user, Sort sort);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.createdAt BETWEEN :startDate AND :endDate")
    List<Expense> findByUserAndCreatedAtBetween(@Param("user") User user,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);


}
