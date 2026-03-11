package com.asinglah.backend.DTO.ExpenseResponseDTO;

import java.math.BigDecimal;

public interface OweExpensesDetailResDTO {
    String getExpenseDetails();
    BigDecimal getAmount();
    Integer getPendingStatus();
    Long getExpenseSplitId();
}
