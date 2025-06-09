package com.nicolas.expenses_tracker.controller.expense;

import com.nicolas.expenses_tracker.dto.expense.ExpenseDTO;
import com.nicolas.expenses_tracker.dto.expense.MonthlyExpenseDTO;
import com.nicolas.expenses_tracker.exception.UserNotFoundException;
import com.nicolas.expenses_tracker.model.user.User;
import com.nicolas.expenses_tracker.service.expense.ExpenseService;
import com.nicolas.expenses_tracker.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private final UserService userService;


    /**
     * Endpoint designed to create an expense for a given user.
     * Expects a valid ExpenseDTO and an authenticated user.
     */
    @PostMapping("/expenses")
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody @Valid ExpenseDTO expenseDTO,
                                                    Authentication authentication) {
        User user =
                userService.getUserByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(expenseService.saveExpense(expenseDTO, user));
    }


    /**
     * Deletes an expense.
     * Requires an authenticated user as well as
     * a valid expense ID, which must also belong
     * to the user.
     */
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<?> deleteExpense(Authentication authentication, @PathVariable long id) {
        User user = getUserFromAuthentication(authentication);
        expenseService.deleteExpense(id, user);
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }


    /**
     * Updates an existing expense.
     * Requires an authenticated user as well as
     * a valid expense ID, which must also belong
     * to the user.
     */
    @PatchMapping("/expenses/{id}")
    public ResponseEntity<?> updateExpense(Authentication authentication,
                                           @PathVariable long id, @RequestBody ExpenseDTO expenseDTO) {
        User user = getUserFromAuthentication(authentication);
        return new ResponseEntity<>(expenseService.updateExpense(id, user, expenseDTO), HttpStatus.OK) ;
    }


    /**
     * Retrieves all the expenses
     * created by the user
     */
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(
            Authentication authentication,
            @RequestParam(required = false) String sort // optional sort parameter
    ) {
        User user = getUserFromAuthentication(authentication);

        // By default, sorts by date
        Sort sorting = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sort != null) {
            // parse "field,direction"
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                sorting = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
            } else {
                sorting = Sort.by(sort);
            }
        }

        List<ExpenseDTO> expenses = expenseService.getAllExpenses(user, sorting);
        return ResponseEntity.ok(expenses);
    }



    /**
     *  Returns a list with ExpenseDTOs based on the date range
     *  specified by the client as well as the
     *
     *  The year must be specified, but the month is optional.
     *  If the month is not provided, all the expenses
     *  for the provided year are returned
     */
    @GetMapping("/expenses/filter")
    public ResponseEntity<List<ExpenseDTO>> filterExpenses(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Float minValue,
            @RequestParam(required = false) Float maxValue,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Authentication authentication) {

        User user = getUserFromAuthentication(authentication);
        List<ExpenseDTO> results = expenseService.filterExpenses(user, year, month, category, minValue, maxValue,
                sortBy, direction);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/expenses/yearly")
    public ResponseEntity<List<MonthlyExpenseDTO>> getYearlyExpenses(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return ResponseEntity.ok(expenseService.getYearlyExpenses(user));
    }



    /**
     * Gets the user object from
     * the authentication object
     */
    private User getUserFromAuthentication(Authentication authentication) {
        return userService.getUserByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }
}
