package com.nicolas.expenses_tracker.service.expense;

import com.nicolas.expenses_tracker.dto.expense.ExpenseDTO;
import com.nicolas.expenses_tracker.dto.expense.MonthlyExpenseDTO;
import com.nicolas.expenses_tracker.exception.ExpenseNotFoundException;
import com.nicolas.expenses_tracker.model.expense.Expense;
import com.nicolas.expenses_tracker.model.user.User;
import com.nicolas.expenses_tracker.repository.expense.ExpenseRepository;
import com.nicolas.expenses_tracker.repository.expense.spec.ExpenseSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nicolas.expenses_tracker.utils.DateUtils.monthLabels;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;


    /**
     * Saves an expense to the Database
     */
    public ExpenseDTO saveExpense(ExpenseDTO expenseDTO, User user) {
        Expense expense = ExpenseDTO.toExpense(expenseDTO);
        expense.setUser(user);
        expenseRepository.save(expense);
        return expenseDTO;
    }


    /**
     * Retrieves all the user's expenses
     */
    public List<ExpenseDTO> getAllExpenses(User user, Sort sort) {
        List<Expense> expenses = expenseRepository.findByUser(user, sort);
        return expenses.stream().map(ExpenseDTO::toDTO).toList();
    }



    /**
     * Checks whether an expense is valid
     */
    private static boolean checkValidExpense(User user, Optional<Expense> expense) {

        if (expense.isPresent() && (expense.get().getUser() != user)) {
            return true;
        }

        return expense.isEmpty();
    }

    /**
     * Deletes an expense.
     * Requires a valid expense ID.
     */
    public void deleteExpense(long id, User user) {
        Optional<Expense> expense = expenseRepository.findById(id);

        boolean isExpenseInvalid = checkValidExpense(user, expense);
        if (isExpenseInvalid) throw new ExpenseNotFoundException();

        expenseRepository.delete(expense.get());
    }


    /**
     * Updates an expense.
     * Requires a valid expense dto which will be used
     * to replace the existing values.
     */
    public ExpenseDTO updateExpense(long id, User user, ExpenseDTO expenseDTO) {
        Optional<Expense> expense = expenseRepository.findById(id);

        boolean isExpenseInvalid = checkValidExpense(user, expense);
        if (isExpenseInvalid) throw new ExpenseNotFoundException();

        expense.get().setCategory(expenseDTO.getCategory());
        expense.get().setValue(expenseDTO.getValue());
        expense.get().setCreatedAt(expenseDTO.getCreatedAt());

        expenseRepository.save(expense.get());
        return ExpenseDTO.toDTO(expense.get());
    }


    /**
     * Returns a list of expenses created by the user, filtered by the given criteria.
     *
     * If no query parameters are provided, it returns all expenses sorted by the
     * "createdAt" field in descending order (most recent first).
     *
     * If query parameters are provided (e.g., year, month, category, price range),
     * only the expenses that match the given filters will be returned.
     *
     * Sorting can be customized by specifying the "sortBy" and "direction" parameters.
     *
     * @param user      the user whose expenses are being queried
     * @param year      the year to filter by (optional)
     * @param month     the month to filter by (optional)
     * @param category  the expense category to filter by (optional)
     * @param minValue  the minimum expense value (optional)
     * @param maxValue  the maximum expense value (optional)
     * @param sortBy    the field to sort by (default: "createdAt")
     * @param direction the sorting direction, "asc" or "desc" (default: "desc")
     * @return a list of ExpenseDTOs matching the filters
     */

    public List<ExpenseDTO> filterExpenses(User user,
                                           Integer year,
                                           Integer month,
                                           String category,
                                           Float minValue,
                                           Float maxValue,
                                           String sortBy,
                                           String direction) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        // Logic for filtering based on the date.
        // If month is null, returns the results for the entire year
        if (year != null) {
            start = LocalDate.of(year, (month != null ? month : 1), 1).atStartOfDay();
            end = (month != null)
                    ? start.plusMonths(1).minusNanos(1)
                    : start.plusYears(1).minusNanos(1);
        }

        // By default shows results in descending order
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Specification<Expense> spec = ExpenseSpecifications.withFilters(user, start, end, category, minValue, maxValue);
        return expenseRepository.findAll(spec, sort).stream()
                .map(ExpenseDTO::toDTO)
                .toList();
    }


    /**
     * Returns a list with all the expenses of the given year
     * up to the current date
     */
    public List<MonthlyExpenseDTO> getYearlyExpenses(User user) {
        LocalDateTime currentDay = LocalDateTime.now();
        LocalDateTime firstDayOfTheYear = LocalDate.of(LocalDateTime.now().getYear(), 1, 1).atStartOfDay();

        // Gets all the user's expenses up to the current day
        List<ExpenseDTO> yearlyExpenses = expenseRepository.findByUserAndCreatedAtBetween(user, firstDayOfTheYear,
                        currentDay)
                .stream().map(ExpenseDTO::toDTO).toList();

        // Defines a list of lists where each sublist represents a month
        List<List<ExpenseDTO>> monthlyExpensesDTOs = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            monthlyExpensesDTOs.add(new ArrayList<>());
        }

        // Adds the expenses to the corresponding index in the list
        // e.g January expenses will be at index 0, December at index 11
        yearlyExpenses.forEach((expense) -> {
            monthlyExpensesDTOs.get(expense.getCreatedAt().getMonth().getValue() - 1).add(expense);
        });

        // Converts every ExpenseDTO object to the value it holds
        List<List<Float>> monthlyExpensesValues = monthlyExpensesDTOs.stream().map((expenses) ->
                expenses.stream().map(ExpenseDTO::getValue).toList()).toList();

        // Returns a list with the sum of all the monthly expenses
        List<Float> monthlyTotals = monthlyExpensesValues.stream().map((expenses) -> {
            return expenses.stream().reduce(0f, Float::sum);
        }).toList();

        List<MonthlyExpenseDTO> results = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            results.add(new MonthlyExpenseDTO(monthlyTotals.get(i), monthLabels.get(i)));
        }

        return results;
    }
}
