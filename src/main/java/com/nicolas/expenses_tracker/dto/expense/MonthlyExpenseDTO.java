package com.nicolas.expenses_tracker.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for sending monthly expenses summaries to the client.
 * The class is intended for client input (read-only), and
 * should not be sent to the server.
 */
@Data
@AllArgsConstructor
public class MonthlyExpenseDTO {

    private float value;

    private String month;

}
